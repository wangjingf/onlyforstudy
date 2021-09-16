package io.study.rest.controller;

import com.jd.vd.common.util.ResultUtil;
import com.jd.vd.common.web.util.RequestHelper;
import io.study.dao.Document;
import io.study.rest.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Controller
@RequestMapping(value="/doc")
public class IndexController {
    @Autowired
    DocumentService documentService;

    @RequestMapping(value="/index",method= RequestMethod.GET)
    @ResponseBody
    public String index(HttpServletRequest request){
        return "it is ok";
    }

    @RequestMapping(value="/add",method= RequestMethod.POST)
    @ResponseBody
    public ResultUtil addDoc(HttpServletRequest request){
        Document document = RequestHelper.requestBean(request, Document.class);
        Serializable id = documentService.saveDoc(document);
        return ResultUtil.success(id);
    }
}
