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
    String name;
    String activity;
    String lastBuildLabel;
    String lastBuildStatus;
    String lastBuildTime;
    String webUrl;
}
