package org.developfreedom.ccdroid.app;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class Project {
    String name;
    String activity;
    String lastBuildLabel;
    String lastBuildStatus;
    String lastBuildTime;
    String webUrl;

    public Project(String a, String b, String c, String d, String e, String f) {
        this.name = a;
        this.activity = b;
        this.lastBuildLabel = c;
        this.lastBuildStatus = d;
        this.lastBuildTime = e;
        this.webUrl = f;
    }
}
