<%@ tag description="Panel to notify students of new/edited comments" %>
<%@ tag import="teammates.common.util.Const" %>
<%@ attribute name="studentCommentClearPendingLink" required="true" %>
<%@ attribute name="numberOfPendingComments" required="true" %>
<div class="btn-group pull-right" style="${numberOfPendingComments==0 ? 'display:none' : ''}">
    <a type="button" class="btn btn-sm btn-info" data-toggle="tooltip" style="margin-right: 17px;"
        href="${studentCommentClearPendingLink}"
        title="Send email notification to ${numberOfPendingComments} recipient(s) of comments pending notification">
        <span class="badge" style="margin-right: 5px">${numberOfPendingComments}</span>
        <span class="glyphicon glyphicon-comment"></span>
        <span class="glyphicon glyphicon-arrow-right"></span>
        <span class="glyphicon glyphicon-envelope"></span>
    </a>
</div>