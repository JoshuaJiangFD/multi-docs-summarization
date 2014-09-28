package fudan.mmdb.mds.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    protected static final String HOME_VIEW = "index";

    protected static final String QEURY_VIEW="queryres";
        
    @RequestMapping(value ={"/"}, method = RequestMethod.GET)
    public String showHomePage() {
        LOGGER.debug("Rendering home page.");
        return "index";
    }
    
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public String showQueryPage() {
        LOGGER.debug("Rendering query page.");
        return QEURY_VIEW;
    }
}
