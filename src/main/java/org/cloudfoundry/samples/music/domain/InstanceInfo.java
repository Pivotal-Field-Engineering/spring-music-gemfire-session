package org.cloudfoundry.samples.music.domain;

import org.springframework.cloud.app.ApplicationInstanceInfo;

public class InstanceInfo {
    public String getInstanceIndex() {
        return instanceIndex;
    }

    private String instanceIndex;

    public String getInstanceId() {
        return instanceId;
    }

    private String instanceId;

    public InstanceInfo(ApplicationInstanceInfo applicationInstanceInfo){
        this.instanceIndex = applicationInstanceInfo.getProperties().get("instance_index").toString();
        this.instanceId = applicationInstanceInfo.getInstanceId();
    }


}
