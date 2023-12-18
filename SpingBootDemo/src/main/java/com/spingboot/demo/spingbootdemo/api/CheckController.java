package com.spingboot.demo.spingbootdemo.api;

import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.response.BaseResponse;
import com.spingboot.demo.spingbootdemo.response.ResponseUtils;
import com.spingboot.demo.spingbootdemo.utils.Base64Utils;
import com.spingboot.demo.spingbootdemo.utils.ClientIpUtils;
import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class CheckController {

    @Autowired
    private HttpServletRequest request;
    @GetMapping("/checkLocal")
    public ResponseEntity<BaseResponse> checkLocal(){
        IPinfo ipInfo = new IPinfo.Builder()
                .setToken(Base64Utils.decrypt("MzE0MDE4MTc1N2JiOTA="))
                .build();
        try {
            IPResponse response = ipInfo.lookupIP(ClientIpUtils.getClientIP(request));
            if (response.getCountryCode() == null || response.getCountryCode().equals("US") ){
                return  ResponseUtils.responseSuccess("Check Success",null);
            }
        } catch (RateLimitedException e) {
            throw new RuntimeException(e);
        }

        try {
            return ResponseUtils.responseError("Check Filed", "当前地区没有权限",Mark.ERROR_BASE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
