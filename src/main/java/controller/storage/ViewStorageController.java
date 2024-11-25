package controller.storage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import model.domain.Content;
import model.service.StorageManager;


public class ViewStorageController implements Controller{
    //로그인 되어있는지 검사 로직 추가필요
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        //로그인 시 세션에 consumerId 저장하고 사용하는 것으로 수정하기
        String consumerId = request.getParameter("consumerId");
        
        StorageManager manager = StorageManager.getInstance();
        List<Content> contentList = manager.showStorage(consumerId);
        
        // contentList 객체를 request에 저장하여 커뮤니티 보관함 화면으로 이동
        request.setAttribute("storage", contentList);             
        return "/storage/view.jsp";  
    }
    
}
