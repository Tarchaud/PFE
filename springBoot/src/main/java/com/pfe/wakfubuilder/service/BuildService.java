package com.pfe.wakfubuilder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pfe.wakfubuilder.model.Build;

@Service
public class BuildService {

    static private ArrayList<Build> builds = new ArrayList<>(Arrays.asList(
            new Build(1, "build 1", 1, Build.Cost.low),
            new Build(2, "build 2", 2, Build.Cost.medium),
            new Build(3, "build 3", 3, Build.Cost.high),
            new Build(4, "build 4", 4, Build.Cost.low)
    ));

    public List<Build> getBuilds() {
        return builds;
    }

    public Build getBuild(long id) {
        return builds.stream().filter(build -> build.getId() == id).findFirst().orElse(null);
    }

    public void deleteBuild(long id) {
        builds.removeIf(build -> build.getId() == id);
    }

    public static void addBuild(Build build) {
        builds.add(build);
    }

    public void updateBuild(long id, Build build) {
        builds.forEach(buildlambda -> {
            if (buildlambda.getId() == id) {
                builds.set(builds.indexOf(buildlambda), build);
            }
        });
    }
}
