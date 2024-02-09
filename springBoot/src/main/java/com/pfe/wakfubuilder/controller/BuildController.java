package com.pfe.wakfubuilder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pfe.wakfubuilder.model.Build;
import com.pfe.wakfubuilder.model.BuildRequest;
import com.pfe.wakfubuilder.service.BuildService;

@RestController
public class BuildController {

    @Autowired
    private BuildService buildService;

    @GetMapping("/builds")
    public List<Build> getBuilds() {
        return buildService.getBuilds();
    }

    @GetMapping("/build/{id}")
    public Build getBuild(@PathVariable String id) {
        return buildService.getBuild(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value ="build/{id}")
    public void deleteBuild(@PathVariable String id) {
        buildService.deleteBuild(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "builds")
    public void saveBuild(@RequestBody Build build) {
        buildService.saveBuild(build);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "build/{id}")
    public void updateBuild(@PathVariable String id, @RequestBody Build build) {
        buildService.updateBuild(id, build);
    }

    @RequestMapping(method = RequestMethod.POST, value = "generateBuild")
    public Build generateBuild(@RequestBody BuildRequest buildRequest) {
        return buildService.generateBuild(buildRequest.getName(), buildRequest.getLevel(), buildRequest.getCost(), buildRequest.getEffects());
    }
}