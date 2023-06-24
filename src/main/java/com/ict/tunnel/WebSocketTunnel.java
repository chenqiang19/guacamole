package com.ict.tunnel;

import com.ict.config.ParamConfig;
import com.ict.config.SpringBeanFactory;
import com.ict.db.entity.GuacamoleConnectEntity;
import com.ict.db.entity.GuacamoleConnectParameterEntity;
import com.ict.db.service.GuacamoleConnectParameterService;
import com.ict.db.service.GuacamoleConnectService;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleClientInformation;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.websocket.GuacamoleWebSocketTunnelEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @Title: WebSocketTunnel
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2021/8/2
 **/
@ServerEndpoint(value = "/webSocket", subprotocols = "guacamole")
@Component
public class WebSocketTunnel extends GuacamoleWebSocketTunnelEndpoint {

    @Autowired
    private ParamConfig paramConfig;

    private static GuacamoleConnectService guacamoleConnectService;
    @Autowired
    public void setGuacamoleConnectService(GuacamoleConnectService guacamoleConnectService) {
        WebSocketTunnel.guacamoleConnectService = guacamoleConnectService;
    }

    private static GuacamoleConnectParameterService guacamoleConnectParameterService;
    @Autowired
    public void setGuacamoleConnectParameterService(GuacamoleConnectParameterService guacamoleConnectParameterService) {
        WebSocketTunnel.guacamoleConnectParameterService = guacamoleConnectParameterService;
    }

    /**
     * Returns a new tunnel for the given session. How this tunnel is created
     * or retrieved is implementation-dependent.
     *
     * @param session        The session associated with the active WebSocket
     *                       connection.
     * @param endpointConfig information associated with the instance of
     *                       the endpoint created for handling this single connection.
     * @return A connected tunnel, or null if no such tunnel exists.
     * @throws GuacamoleException If an error occurs while retrieving the
     *                            tunnel, or if access to the tunnel is denied.
     */
    @Override
    protected GuacamoleTunnel createTunnel(Session session, EndpointConfig endpointConfig) throws GuacamoleException {
        //获取html中创建Guacamole.WebSocketTunnel URL中的参数信息
        final Map<String, List<String>> requestParameters = session.getRequestParameterMap();
        GuacamoleClientInformation information = new GuacamoleClientInformation();
        List<String> heightValue = requestParameters.get("height");
        if (heightValue !=null && heightValue.size() > 0) {
            Integer height = Integer.valueOf(heightValue.get(0));
            information.setOptimalScreenHeight(height);
        }

        List<String> widthValue = requestParameters.get("width");
        if (widthValue !=null && widthValue.size() > 0) {
            Integer width = Integer.valueOf(widthValue.get(0));
            information.setOptimalScreenWidth(width);
        }
        //获取连接的实例ID
        List<String> instanceId = requestParameters.get("instance_id");
        if (instanceId == null || instanceId.size() == 0) {
            throw new InvalidParameterException("实例ID为空");
        }

        //获取Guacd数据库中，实例的信息
        List<GuacamoleConnectEntity> connectEntities = guacamoleConnectService.selectByInstanceId(instanceId.get(0));
        if (connectEntities == null) {
            throw new InvalidParameterException("实例信息未写入数据库");
        }

        if (connectEntities.size() == 0 || connectEntities.size() >1) {
            throw new InvalidParameterException("实例不存在");
        }

        GuacamoleConnectEntity guacamoleConnectEntity = connectEntities.get(0);
        String protocol = guacamoleConnectEntity.getProtocol();
        //获取实例的连接参数
        // hostname, port, username, password, ignore-cert, security
        List<GuacamoleConnectParameterEntity> guacamoleConnectParameterEntities = guacamoleConnectParameterService.selectByConnectId(guacamoleConnectEntity.getConnectionId());

        // 获取url的值
        String remoteHostname = null;
        String remotePort = null;
        String username = null;
        String password = null;
        String ignoreCert = null;
        String security = null;
        for (GuacamoleConnectParameterEntity param : guacamoleConnectParameterEntities) {
            if (param.getParameterName().equals("hostname")) {
                remoteHostname = param.getParameterValue();
                continue;
            }

            if (param.getParameterName().equals("port")) {
                remotePort = param.getParameterValue();
                continue;
            }

            if (param.getParameterName().equals("username")) {
                username = param.getParameterValue();
                continue;
            }

            if (param.getParameterName().equals("password")) {
                password = param.getParameterValue();
                continue;
            }

            if (param.getParameterName().equals("ignorecert") || param.getParameterName().equals("ignore-cert")) {
                ignoreCert = param.getParameterValue();
                continue;
            }

            if (param.getParameterName().equals("security")) {
                security = param.getParameterValue();
                continue;
            }
        }

        if (ignoreCert == null) {
            ignoreCert = String.valueOf(true);
        }

        if (security == null) {
            security = String.valueOf(false);
        }

        if (protocol == null || protocol == "") {
            throw new IllegalArgumentException("Guacd协议为空");
        }else if (!protocol.equals("rdp") && !protocol.equals("vnc") && !protocol.equals("ssh")) {
            throw new IllegalArgumentException("Guacd协议不支持，目前仅支持(rdp, vnc, ssh)");
        }

        // 配置Guacamole
        GuacamoleConfiguration configuration = new GuacamoleConfiguration();
        configuration.setProtocol(protocol); //"rdp"
        // 远程windows服务的地址
        if (remoteHostname == null || remoteHostname == "")
            throw new GuacamoleException("Guacd连接的远程主机为空");
        if (remotePort == null)
            throw new GuacamoleException("Guacd连接的远程端口为空");

        configuration.setParameter("hostname", remoteHostname); //"10.10.1.120"
        configuration.setParameter("port", remotePort); //"3389"

        if (username != null) {
            configuration.setParameter("username", username); //"administrator"
        }

        if (password != null) {
            configuration.setParameter("password", password); //"smartcore.123"
        }

        if (ignoreCert != null) {
            configuration.setParameter("ignore-cert", ignoreCert); //true
        }

        if (security != null) {
            configuration.setParameter("security", security); //any
        }
//        if (protocol.equals("ssh")) {
//            configuration.setParameter("font-name", "DejaVuSansMono");
//            configuration.setParameter("font-size", "12");
//        }

        String fileName = getNowTime() + ".guac";//文件名
        String outputFilePath = "/home/guacamole";
        //添加会话录制--录屏
        configuration.setParameter("recording-path", outputFilePath);
        configuration.setParameter("create-recording-path", "true");
        configuration.setParameter("recording-name", fileName);

        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                new InetGuacamoleSocket(paramConfig.getGuacdIp(), paramConfig.getGuacdPort()),
                configuration,
                information
        );

        GuacamoleTunnel tunnel = new SimpleGuacamoleTunnel(socket);
        return tunnel;
    }

    private void optClose(Session session) {
        // 判断当前连接是否还在线
        if (session.isOpen()) {
            try {
                // 关闭连接
                CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "进行关闭！");
                session.close(closeReason);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String getNowTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
