package fudan.mmdb.mds.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    protected static final String HOME_VIEW = "index";

    protected static final String QEURY_VIEW="query";
        
    @RequestMapping(value ={"/","/index.html"}, method = RequestMethod.GET)
    public String showHomePage() {
        LOGGER.debug("Rendering home page.");
        return HOME_VIEW;
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView showQueryPage(HttpServletRequest request) {
        LOGGER.debug("Rendering search page.");
        ModelAndView mv=new ModelAndView();
        String queryTerm=request.getParameter("q");
        mv.setViewName(QEURY_VIEW);
        mv.addObject("queryterm", queryTerm);
        return mv;
        
    }
}
