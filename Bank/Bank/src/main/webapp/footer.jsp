<% 

	Integer QUERIES = new Integer(0);
	
	if(request.getAttribute("QUERIES") != null)
		QUERIES = (Integer)request.getAttribute("QUERIES");
	

	Long startime = new Long(System.currentTimeMillis());
	if(request.getAttribute("START") != null)
		startime = (Long)request.getAttribute("START");
	
	long end = System.currentTimeMillis();
	long start = startime.longValue();
	long diff = end - start;
	float elapse_time = (float)diff / 1000;

%>
	</div>
	<div id="footer">
		<span>Copyright &copy; 2005 <a href="http://www.m0interactive.com">m0 | interactive</a>. All rights reserved. Page loaded in <%=elapse_time %> seconds, using <%=QUERIES %> query(s)!</span>
		<span> Also: <a href="http://validator.w3.org/check?uri=referer" title="Valid XHTML!">XHTML</a>, <a href="http://jigsaw.w3.org/css-validator/check/referer" title="Valid CSS!">CSS</a>, <a href="http://www.contentquality.com/mynewtester/cynthia.exe?Url1=http://m0.myftp.org:35456/Bank/" title="Valid Content Accessibility Report!">508</a></span>
	</div>
</div>
</body>
</html>