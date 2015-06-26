package org.developfreedom.ccdroid.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Project {
    private String name;
    private String activity;
    private String lastBuildLabel;
    private String lastBuildStatus;
    private String lastBuildTime;
    private String webUrl;

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", activity='" + activity + '\'' +
                ", lastBuildLabel='" + lastBuildLabel + '\'' +
                ", lastBuildStatus='" + lastBuildStatus + '\'' +
                ", lastBuildTime='" + lastBuildTime + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
