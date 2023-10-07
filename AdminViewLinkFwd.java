
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/viewadmin")
public class AdminViewLinkFwd extends HttpServlet {

	TrainService trainService = new TrainServiceImpl();

	/**
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);
		try {
			String trainNo = req.getParameter("trainNo");
			TrainBean train = trainService.getTrainById(trainNo);
			if (train != null) {
				RequestDispatcher rd = req.getRequestDispatcher("AdminHome.html");
				rd.include(req, res);
				pw.println("<div class='main'><p1 class='menu'>Train Detail as per user request</p1></div>");
				pw.println("<div class='tab'>" + "<table>" + "<tr><td class='blue'>Train Name :</td><td>"
						+ train.getTr_name() + "</td></tr>" + "<tr><td class='blue'>Train Number :</td><td>"
						+ train.getTr_no() + "</td></tr>" + "<tr><td class='blue'>Departure:</td><td>"
						+ train.getDeparture() + "</td></tr>" + "<tr><td class='blue'>Destination:</td><td>"
						+ train.getDestination() + "</td></tr>" + "<tr><td class='blue'> Total Available Seats:</td><td>"
						+ train.getTotalseats() + "</td></tr>" + "<tr><td class='blue'>Total Fare(INR) :</td><td>"
						+ train.getTotalfare() + " RS</td></tr>" + "</table>" + "</div>");
			} else {
				RequestDispatcher rd = req.getRequestDispatcher("AdminSearchTrains.html");
				rd.include(req, res);
				pw.println("<div class='tab'><p1 class='menu'>Train No." + req.getParameter("trainnumber")
						+ " is Not Available !</p1></div>");
			}
		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());

		}

	}

}
