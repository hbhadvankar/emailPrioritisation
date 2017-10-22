package test;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.prioritization.dao.EmailDao;
import com.prioritization.dao.EmailDaoImpl;
import com.prioritization.dao.PriorityDaoImpl;

public class EmailTest {

	public static void applyPrioritiesToEmailMethod(String email) throws Exception {
		try {
			PriorityDaoImpl priorityDao = new PriorityDaoImpl();
			JSONArray priorityJsonArray = priorityDao.getAllPriorities();
			DBObject dbObject = (DBObject) JSON.parse(email);
			int latestApplyOrderValue = 10000;
			for (Object object : priorityJsonArray) {
				JSONObject jsonObject = (JSONObject) object;
				JSONObject conditionJsonObject = (JSONObject) jsonObject.get("condition");
				if(conditionJsonObject!= null){
					String conditionParameter = (String) conditionJsonObject.get("parameter");
					String conditionParameterValue = (String) conditionJsonObject.get("value");
					String priorityLabel = (String) jsonObject.get("priorityLabel");
					
					int applyOrder = (int) conditionJsonObject.get("applyOrder");
					if ("email".equalsIgnoreCase(conditionParameter)){
						String fromEmailId = (String) dbObject.get("from_emailId");
						if(fromEmailId != null 
								&& fromEmailId.equalsIgnoreCase(conditionParameterValue)
								&& latestApplyOrderValue > applyOrder){
							latestApplyOrderValue = applyOrder;
							dbObject.put("priorityLabel", priorityLabel);
							/*
							 * Apply Label to email
							 */
						}
					} else if ("subject".equalsIgnoreCase(conditionParameter)){
						String subject = (String) dbObject.get("subject");
						if(subject != null 
								&& subject.contains(conditionParameterValue)
								&& latestApplyOrderValue > applyOrder){
							latestApplyOrderValue = applyOrder;
							dbObject.put("priorityLabel", priorityLabel);
							/*
							 * Apply Label to email
							 */
						}
					}
				}

			}
			//System.out.println(dbObject);
			//emailDao.addNewEmail(dbObject);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			/*
			 * Commenting exception.getMessage() since we are already printing the message in DAO Implementation.
			 * We are only throwing the exception back to the web service method. 
			 * This is done to send a proper response code in case of any error occured
			 */
			//System.out.println("Exception = "+exception.getMessage());
			throw exception;
		}
	}
	
	public static JSONObject getEmailByIdMethod(String emailId) throws Exception {
		JSONObject jsonObject = null;
		try {
			PriorityDaoImpl priorityDao = new PriorityDaoImpl();
			EmailDao emailDao = new EmailDaoImpl();
			jsonObject = emailDao.getEmailById(emailId);
			String priorityLabel = (String) jsonObject.get("priorityLabel");
			
			JSONArray higherPrioritiesJsonArray = 
					priorityDao.getAllPrioritiesHigherThanCurrentPriority(priorityLabel);
			if(higherPrioritiesJsonArray != null && higherPrioritiesJsonArray.length() > 0){
				for (Object object : higherPrioritiesJsonArray) {
					JSONObject priorityJsonObject = (JSONObject) object;
					String higherPriorityLabel = (String) priorityJsonObject.get("priorityLabel");
					JSONArray unReadEmailsJsonArray = 
							emailDao.findAllUnReadEmailsByPriorityLabel(higherPriorityLabel);
					if(unReadEmailsJsonArray != null && unReadEmailsJsonArray.length() > 0){
						return null;
					}
				}
			}
			
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			/*
			 * Commenting exception.getMessage() since we are already printing the message in DAO Implementation.
			 * We are only throwing the exception back to the web service method. 
			 * This is done to send a proper response code in case of any error occured
			 */
			//System.out.println("Exception = "+exception.getMessage());
			throw exception;
		}
		return jsonObject;
	}
	
	public static void main(String[] args) throws Exception {
		//getAllPrioritiesMethod();
		//applyPrioritiesToEmailMethod("{ \"from_emailId\": \"manager@company.com\", \"to_emailId\": \"abcd@gmail.com\", \"subject\": \"Urgent Work Please Reply\", \"read\": false, \"replied\": false}");
		getEmailByIdMethod("59eb24e98d6a51a06d4b0485");
	}
}
