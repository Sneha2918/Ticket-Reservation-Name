
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
@WebServlet("/trainbwstn")
public class TrainBwStn extends HttpServlet {
	private TrainService trainService = new TrainServiceImpl();

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		TrainUtil.validateUserAuthorization(req, UserRole.CLIENT);
		try {
			String fromStation = req.getParameter("fromstation");
			String toStation = req.getParameter("tostation");
			List<TrainBean> trains = trainService.getTrainsBetweenStations(fromStation, toStation);
			if (trains != null && !trains.isEmpty()) {
				RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
				rd.include(req, res);
				pw.println("<div class='main'><p1 class='menu'>Trains BetWeen Station "
						+ req.getParameter("fromstation") + " and " + req.getParameter("tostation") + "</p1></div>");
				pw.println("<div class='tab'><table><tr><th>Train Name</th><th>Train Number</th>"
						+ "<th>Departure</th><th>Destination</th><th>Time</th><th>Total Seats</th><th>Total Fare(INR)</th><th>Action</th></tr>");
				for (TrainBean train : trains) {
					int hr = (int) (Math.random() * 24);
					int min = (int) (Math.random() * 60);
					String time = (hr < 10 ? ("0" + hr) : hr) + ":" + ((min < 10) ? "0" + min : min);

					pw.println("" + "<tr><td>" + train.getTr_name() + "</td>" + "<td>" + train.getTr_no() + "</td>"
							+ "<td>" + train.getDeparture() + "</td>" + "<td>" + train.getDestination() + "</td>" + "<td>"
							+ time + "</td>" + "<td>" + train.getTotalseats() + "</td>" + "<td>" + train.getTotalfare()
							+ " RS</td><td><a href='booktrainbyref?trainNo=" + train.getTr_no() + "&fromStn="
							+ train.getDeparture() + "&toStn=" + train.getDestination()
							+ "'><div class='red'>Book Now</div></a></td>" + "</tr>");
				}
				pw.println("</table></div>");
			} else {
				RequestDispatcher rd = req.getRequestDispatcher("TrainBwStn.html");
				rd.include(req, res);
				pw.println("<div class='tab'><p1 class='menu'>There are no trains Between "
						+ req.getParameter("fromstation") + " and " + req.getParameter("tostation") + "</p1></div>");
			}
		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}

	}
}
