package controller.storage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.Controller;
//import controller.user.UserSessionUtils;
import controller.consumer.UserSessionUtils;
import model.service.StorageManager;

public class DeleteFavController implements Controller{

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        
        if (!UserSessionUtils.hasLogined(session)) { return "redirect:/user/login/form"; }
        
        request.setCharacterEncoding("utf-8");
        
        String consumerId = UserSessionUtils.getLoginUserId(session);
        String contentId = request.getParameter("contentId");
        String type = request.getParameter("type");
        
        StorageManager manager = StorageManager.getInstance();
        int count = manager.deleteFav(contentId, consumerId);
        
        if (type == null) {
            return "redirect:/content/view"; 
        }
        switch (type) {
            case "movie":
                return "redirect:/content/view?type=movie";
            case "drama":
                return "redirect:/content/view?type=drama";
            case "animation":
                return "redirect:/content/view?type=animation";
            default:
                return "redirect:/content/view";
        }
    }
    
}
