package org.developfreedom.ccdroid.app;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by shubham on 22/03/15.
 */
@Getter
@Setter
public class Project {
    String name;
    String activity;
    String lastBuildLabel;
    String lastBuildStatus;
    String lastBuildTime;
    String webUrl;
}
