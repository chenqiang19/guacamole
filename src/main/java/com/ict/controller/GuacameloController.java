package com.ict.controller;

import com.ict.domain.GuacamoleInfo;
import com.ict.domain.GuacamoleParameter;
import com.ict.service.Impl.GuacamoleServiceImpl;
import com.ict.utils.ParameterConvert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;

@RestController
public class GuacameloController {

    @Resource
    private GuacamoleServiceImpl guacamoleService;

    @PostMapping("/guacamoleInfo")
    public void insertGuacamoleInfo(@Validated @RequestBody @ParameterConvert GuacamoleInfo resources) throws NoSuchAlgorithmException {
        guacamoleService.insertGuacamoleInfo(resources);
    }

    @PutMapping("/parameter/{connectionId}")
    public void modifiedParameter(@Validated @RequestBody @ParameterConvert GuacamoleParameter resources,
                               @PathVariable String connectionId
    ) {
        guacamoleService.modifiedParameter(resources, connectionId);
    }

    @GetMapping("/connection/{connectionId}/parameter/{name}")
    public String getConnectHostname(@PathVariable String connectionId,
                                     @PathVariable String name) {
        return guacamoleService.getConnectHostname(connectionId, name);
    }
}
