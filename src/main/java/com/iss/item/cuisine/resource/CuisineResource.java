package com.iss.item.cuisine.resource;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.iss.framework.Global;
import com.iss.framework.RequireRole;
import com.iss.framework.Util;
import com.iss.item.cuisine.dao.CuisineMapper;
import com.iss.item.cuisine.model.Cuisine;

@RequestMapping(value="/CuisineResource")
@RestController
public class CuisineResource {

	@Resource
	private CuisineMapper mapper;
	
	private final String RESOURCE_PATH = this.getClass().getClassLoader().getResource("../../").getPath();
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/list", method=RequestMethod.GET)
    public List<Cuisine> list() throws Exception{
		List<Cuisine> result = mapper.list();
		for (Cuisine cuisine : result) {
			String image = cuisine.getImage();
			if(image != null && !image.isEmpty()){
				cuisine.setImage("http://localhost:8080/Satiety/image/" + image);
			}
		}
        return result;
    }
	
	@RequestMapping(value="/listActive", method=RequestMethod.GET)
	public List<Cuisine> listActive(){
		List<Cuisine> result = mapper.listActive();
		for (Cuisine cuisine : result) {
			String image = cuisine.getImage();
			if(image != null && !image.isEmpty()){
				cuisine.setImage("http://localhost:8080/Satiety/image/" + image);
			}
		}
		return result;
	}
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/updateState/{id}/{state}", method=RequestMethod.POST)
	public int updateState(@PathVariable("id") int id, @PathVariable("state") int state){
		int result = mapper.updateState(id, state);
		return result;
	}
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public int update(@RequestBody Cuisine cuisine){
		int result = mapper.update(cuisine);
		return result;
	}
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public int save(@RequestBody Cuisine cuisine){
		int result = mapper.save(cuisine);
		return result;
	}
	
	@RequireRole(Global.ROLE_ADMIN)
	@RequestMapping(value="/remove/{id}", method=RequestMethod.POST)
	public int remove(@PathVariable int id){
		int result = mapper.remove(id);
		return result;
	}
	
	@RequestMapping(value="/upload", consumes = "multipart/form-data", method = RequestMethod.POST)
    public String upload(@RequestParam("file") CommonsMultipartFile file) throws IOException {
        String path = RESOURCE_PATH + "\\image\\" + new Date().getTime() + ".png";
        File newFile = new File(path);
        file.transferTo(newFile);
        Util.cutImage(newFile);
        return newFile.getName();
    }
}
