package teammates.common.datatransfer;

import static teammates.common.Common.EOL;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import teammates.common.Assumption;
import teammates.common.Common;
import teammates.common.FieldValidator;
import teammates.common.FieldValidator.FieldType;
import teammates.common.Sanitizer;
import teammates.storage.entity.Submission;

import com.google.appengine.api.datastore.Text;

/**
 * A data transfer object for Submission entities.
 */
public class SubmissionAttributes extends EntityAttributes {
	
	//Note: be careful when changing these variables as their names are used in *.json files.
	public String course; //TODO: rename to courseId 
	public String evaluation; //TODO: rename to evaluationName 
	public String team; //TODO: rename to teamName
	public String reviewer; //TODO: rename to reviewerEmail
	public String reviewee; //TODO: rename to revieweeEmail
	public int points;
	public Text justification;
	public Text p2pFeedback;
	
	@SuppressWarnings("unused")
	private static Logger log = Common.getLogger();
	
	public SubmissionDetailsBundle details = new SubmissionDetailsBundle();

	public SubmissionAttributes() {

	}

	public SubmissionAttributes(String courseId, String evalName, String teamName,
			String toStudent, String fromStudent) {
		this.course = Sanitizer.sanitizeTitle(courseId);
		this.evaluation = Sanitizer.sanitizeTitle(evalName);
		this.team = Sanitizer.sanitizeTitle(teamName);
		this.reviewee = Sanitizer.sanitizeName(toStudent);
		this.reviewer = Sanitizer.sanitizeName(fromStudent);;
		this.justification = Sanitizer.sanitizeTextField(justification);
		this.p2pFeedback = Sanitizer.sanitizeTextField(p2pFeedback);
	}

	public SubmissionAttributes(Submission s) {
		this.course = s.getCourseId();
		this.evaluation = s.getEvaluationName();
		this.reviewer = s.getReviewerEmail();
		this.reviewee = s.getRevieweeEmail();
		this.team = s.getTeamName();
		this.points = s.getPoints();
		this.justification = s.getJustification() == null ? new Text("") : s.getJustification();
		this.p2pFeedback = s.getCommentsToStudent() == null ? new Text("N/A") : s.getCommentsToStudent();
	}

	public Submission toEntity() {
		return new Submission(reviewer, reviewee, course, evaluation, team);
	}

	/* Note: using a simple copy method instead of clone(). Reason: seems it is overly
	 * complicated and not well thought out see
	 * http://stackoverflow.com/questions/2326758/how-to-properly-override-clone-method
	 */
	 /**
	 * @return a copy of the object
	 */
	public SubmissionAttributes getCopy() {
		SubmissionAttributes copy = new SubmissionAttributes();
		copy.course = this.course;
		copy.evaluation = this.evaluation;
		copy.team = this.team;
		copy.reviewer = this.reviewer;
		copy.details.reviewerName = this.details.reviewerName;
		copy.reviewee = this.reviewee;
		copy.details.revieweeName = this.details.revieweeName;
		copy.points = this.points;
		copy.justification = new Text(justification == null ? null
				: justification.getValue());
		copy.p2pFeedback = new Text(p2pFeedback == null ? null
				: p2pFeedback.getValue());
		copy.details.normalizedToStudent = this.details.normalizedToStudent;
		copy.details.normalizedToInstructor = this.details.normalizedToInstructor;
		return copy;
	}

	public boolean isSelfEvaluation() {
		return reviewee.equals(reviewer);
	}

	public List<String> getInvalidityInfo() {
		
		Assumption.assertTrue(justification != null);
		//p2pFeedback can be null if p2p feedback is not enabled;
		
		FieldValidator validator = new FieldValidator();
		List<String> errors = new ArrayList<String>();
		String error;
		
		error= validator.getInvalidityInfo(FieldType.COURSE_ID, course);
		if(!error.isEmpty()) { errors.add(error); }
		
		error = validator.getInvalidityInfo(FieldType.EVALUATION_NAME, evaluation);
		if(!error.isEmpty()) { errors.add(error); }
		
		error = validator.getInvalidityInfo(FieldType.TEAM_NAME, team);
		if(!error.isEmpty()) { errors.add(error); }
		
		error = validator.getInvalidityInfo(FieldType.EMAIL, 
				"email address for the student receiving the evaluation", reviewee);
		if(!error.isEmpty()) { errors.add(error); }
		
		error = validator.getInvalidityInfo(FieldType.EMAIL, 
						"email address for the student giving the evaluation", reviewer);
		if(!error.isEmpty()) { errors.add(error); }
	
		return errors;
	}

	public String toString() {
		return toString(0);
	}

	public String toString(int indent) {
		String indentString = Common.getIndent(indent);
		StringBuilder sb = new StringBuilder();
		sb.append(indentString + "[eval:" + evaluation + "] " + reviewer + "->"
				+ reviewee + EOL);
		sb.append(indentString + " points:" + points);
		sb.append(" [normalized-to-student:" + details.normalizedToStudent + "]");
		sb.append(" [normalized-to-instructor:" + details.normalizedToStudent + "]");
		sb.append(EOL + indentString + " justificatoin:"
				+ justification.getValue());
		sb.append(EOL + indentString + " p2pFeedback:" + p2pFeedback.getValue());
		return sb.toString();
	}

	@Override
	public String getIdentificationString() {
		return this.course + "/" + this.evaluation
				+ " | to: " + this.reviewee + " | from: "
				+ this.reviewer;
	}

	@Override
	public String getEntityTypeAsString() {
		return "Submission";
	}

}
