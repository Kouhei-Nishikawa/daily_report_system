package controllers.follows;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowsShowServlet
 */
@WebServlet("/follow/show")
public class FollowsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowsShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        List<Follow> follows = em.createNamedQuery("getMyAllFollows", Follow.class)
                                  .setParameter("employee", login_employee)
                                  .setFirstResult(10 * (page - 1))
                                  .setMaxResults(10)
                                  .getResultList();

        long follows_count = (long)em.createNamedQuery("getFollowsCount", Long.class)
                                  .setParameter("employee", login_employee)
                                  .getSingleResult();

        request.setAttribute("follows", follows);
        request.setAttribute("follows_count", follows_count);
        request.setAttribute("page", page);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follow/Follow.jsp");
        rd.forward(request, response);

    }

}
