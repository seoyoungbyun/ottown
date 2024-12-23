package controller.storage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.Controller;
import controller.consumer.UserSessionUtils;
import model.domain.Content;
import model.service.StorageManager;


public class ViewStorageController implements Controller{

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        
        if (!UserSessionUtils.hasLogined(session)) { return "redirect:/consumer/login"; }
		 
        request.setCharacterEncoding("utf-8");
        
        String consumerId = UserSessionUtils.getLoginUserId(session);
        StorageManager manager = StorageManager.getInstance();
        List<Content> contentList = manager.showStorage(consumerId);
        
        if (contentList == null || contentList.isEmpty()) {
            contentList = new ArrayList<>(); // 빈 리스트로 초기화
        }
        
        // contentList 객체를 request에 저장하여 커뮤니티 보관함 화면으로 이동
        request.setAttribute("storage", contentList);             
        return "/storage/view.jsp";  
    }
    
}
