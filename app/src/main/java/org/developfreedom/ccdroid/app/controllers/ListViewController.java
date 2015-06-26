package org.developfreedom.ccdroid.app.controllers;

import org.developfreedom.ccdroid.app.Project;

import java.util.List;

public interface ListViewController {
    void updateListView();
    void updateListView(List<Project> projects);
}
