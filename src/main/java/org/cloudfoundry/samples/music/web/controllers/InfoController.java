package org.cloudfoundry.samples.music.web.controllers;

import org.cloudfoundry.samples.music.domain.ApplicationInfo;
import org.cloudfoundry.samples.music.domain.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.app.BasicApplicationInstanceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class InfoController {
    @Autowired(required = false)
    private Cloud cloud;

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    private Environment springEnvironment;

    @Autowired
    public InfoController(Environment springEnvironment) {
        this.springEnvironment = springEnvironment;
    }

    @ResponseBody
    @RequestMapping(value = "/info")
    public ApplicationInfo info() {
        return new ApplicationInfo(springEnvironment.getActiveProfiles(), getServiceNames());
    }

    @ResponseBody
    @RequestMapping(value="/instance")
    public InstanceInfo instance(){
        ApplicationInstanceInfo aii;
        if(cloud == null){
            try{
                CloudFactory cloudFactory = new CloudFactory();
                cloud = cloudFactory.getCloud();

            } catch (CloudException ce) {
                logger.error(ce.toString());
                return new InstanceInfo(
                        new BasicApplicationInstanceInfo(
                               "","", new Hashtable<String, Object>()
                        ));
            }
        }
        aii = cloud.getApplicationInstanceInfo();

        return new InstanceInfo(
                aii
        );
    }


    @RequestMapping(value = "/env")
    @ResponseBody
    public Map<String, String> showEnvironment() {
        return System.getenv();
    }

    @RequestMapping(value = "/service")
    @ResponseBody
    public List<ServiceInfo> showServiceInfo() {
        if (cloud != null) {
            return cloud.getServiceInfos();
        } else {
            return new ArrayList<ServiceInfo>();
        }
    }

    private String[] getServiceNames() {
        if (cloud != null) {
            final List<ServiceInfo> serviceInfos = cloud.getServiceInfos();

            List<String> names = new ArrayList<String>();
            for (ServiceInfo serviceInfo : serviceInfos) {
                names.add(serviceInfo.getId());
            }
            return names.toArray(new String[names.size()]);
        } else {
            return new String[]{};
        }
    }
}