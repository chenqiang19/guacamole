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
import org.apache.guacamole.servlet.GuacamoleHTTPTunnelServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

/**
* @Title: HttpTunnelServlet
* @Description:
* @Version:1.0.0
* @Since:jdk1.8
* @Date  2021/7/2
**/
@WebServlet(urlPatterns = "/tunnel")
public class HttpTunnelServlet extends GuacamoleHTTPTunnelServlet {

    @Autowired
    private ParamConfig paramConfig;

    private static GuacamoleConnectService guacamoleConnectService;

    @Autowired
    public void setGuacamoleConnectService(GuacamoleConnectService guacamoleConnectService) {
        HttpTunnelServlet.guacamoleConnectService = guacamoleConnectService;
    }

    private static GuacamoleConnectParameterService guacamoleConnectParameterService;
    @Autowired
    public void setGuacamoleConnectParameterService(GuacamoleConnectParameterService guacamoleConnectParameterService) {
        HttpTunnelServlet.guacamoleConnectParameterService = guacamoleConnectParameterService;
    }
	@Override
	protected GuacamoleTunnel doConnect(HttpServletRequest request) throws GuacamoleException {

        final Map<String, String[]> requestParameters = request.getParameterMap();
        GuacamoleClientInformation information = new GuacamoleClientInformation();
        String[] heightValue = requestParameters.get("height");
        if (heightValue !=null && heightValue.length > 0) {
            Integer height = Integer.valueOf(heightValue[0]);
            information.setOptimalScreenHeight(height);
        }

        String[] widthValue = requestParameters.get("width");
        if (widthValue !=null && widthValue.length > 0) {
            Integer width = Integer.valueOf(widthValue[0]);
            information.setOptimalScreenWidth(width);
        }
        //获取连接的实例ID
        String[] instanceId = requestParameters.get("instance_id");
        if (instanceId == null || instanceId.length == 0) {
            throw new InvalidParameterException("实例ID为空");
        }

        //获取Guacd数据库中，实例的信息
        List<GuacamoleConnectEntity> connectEntities = guacamoleConnectService.selectByInstanceId(instanceId[0]);
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

        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                new InetGuacamoleSocket(paramConfig.getGuacdIp(), paramConfig.getGuacdPort()),
                configuration,
                information
        );

        GuacamoleTunnel tunnel = new SimpleGuacamoleTunnel(socket);
        return tunnel;
	}
}
