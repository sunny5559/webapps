<%@page import="org.hibernate.internal.SessionFactoryImpl"%>
<%@page import="org.hibernate.SessionFactory"%>
<%@page import="org.hibernate.engine.spi.SessionFactoryImplementor"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.sun.xml.internal.ws.client.RequestContext"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@ page import="org.hibernate.Session,
org.hibernate.stat.Statistics,
org.hibernate.stat.EntityStatistics,
org.hibernate.stat.QueryStatistics,
org.hibernate.stat.SecondLevelCacheStatistics,
java.util.*"%>

<html>
<body>
<%


	ApplicationContext applicationContext = WebApplicationContextUtils
			.getWebApplicationContext(getServletContext());
	SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
	  
	
	    Statistics statistics = sessionFactory.getStatistics();	    
	    statistics.setStatisticsEnabled(true);     
	    long outstandingSessionsOnEntry =  statistics.getSessionOpenCount()         - statistics.getSessionCloseCount();
%>






<table bgcolor="#dddddd" align=center width=60% border=2>
<thead>
<tr><th colspan=2><B>General stats</B></th></tr>
</thead>
<tr><td>Connect count</td><td><%=statistics.getConnectCount()%></td></tr>
<tr><td>SessionOpen Count</td><td><%=statistics.getSessionOpenCount()%></td></tr>
<tr><td>SessionClose Count</td><td><%=statistics.getSessionCloseCount()%></td></tr>
<tr><td>Flush Count</td><td><%=statistics.getFlushCount()%></td></tr>
<tr><td>Entity Load Count</td><td><%=statistics.getEntityLoadCount()%></td></tr>
<tr><td>Entity Fetch Count</td><td><%=statistics.getEntityFetchCount()%></td></tr>
<tr><td>Entity Insert Count</td><td><%=statistics.getEntityInsertCount()%></td></tr>
<tr><td>Entity Update Count</td><td><%=statistics.getEntityUpdateCount()%></td></tr>

<tr><td>Collection Update Count</td><td><%=statistics.getCollectionUpdateCount()%></td></tr>
<tr><td>Collection Remove Count</td><td><%=statistics.getCollectionRemoveCount()%></td></tr>
<tr><td>Collection Load Count</td><td><%=statistics.getCollectionLoadCount()%></td></tr>

<tr><td>Prepare Statement Count</td><td><%=statistics.getPrepareStatementCount()%></td></tr>
<tr><td>CloseStatement Count</td><td><%=statistics.getCloseStatementCount()%></td></tr>

<tr><td>QueryExecution Max Time</td><td><%=statistics.getQueryExecutionMaxTime()%></td></tr>
<tr><td>Query CacheHit Count</td><td><%=statistics.getQueryCacheHitCount()%></td></tr>
<tr><td>Query Cache Miss Count</td><td><%=statistics.getQueryCacheMissCount()%></td></tr>
<tr><td>Query Cache Put Count</td><td><%=statistics.getQueryCachePutCount()%></td></tr>

<tr><td>SecondLevelCache HitCount</td><td><%=statistics.getSecondLevelCacheHitCount()%></td></tr>
<tr><td>SecondLevelCache MissCount</td><td><%=statistics.getSecondLevelCacheMissCount()%></td></tr>
<tr><td>SecondLevelCache PutCount</td><td><%=statistics.getSecondLevelCachePutCount()%></td></tr>

</table>
<br><br><br>
<table bgcolor="#dddd88" align=center width=60% border=2 id="entityStats">
<%
try
{
String[] entityNames = statistics.getEntityNames();
%>

<thead>
<tr><th colspan=6><B>Entity stats [number of entities=<%=entityNames.length%>]</B></th></tr>
<%
String strColumnHeaders =
"<tr><th>class</th><th>Loads</th><th>fetch</th><th>Updates</th><th>Inserts</th><th>deletes</th></tr>";
%>

</thead>
<%


for(int entityIndex=0;entityIndex<entityNames.length;entityIndex ++)
{
EntityStatistics entityStatistics =statistics.getEntityStatistics(entityNames[entityIndex]);
%>
<%if(entityIndex%20==0){%> <%=strColumnHeaders%><%}%>
<tr><td><%=entityNames[entityIndex]%></td>
<td><%=entityStatistics.getLoadCount()%></td>
<td><%=entityStatistics.getFetchCount()%></td>
<td><%=entityStatistics.getUpdateCount()%></td>
<td><%=entityStatistics.getInsertCount()%></td>
<td><%=entityStatistics.getDeleteCount()%></td>
</tr>
<%
}
}
catch(Exception exp)
{
exp.printStackTrace(response.getWriter());
}
%>
</table>

<br><br><br>
<table bgcolor="#bbffff"align=center width=60% border=2 id="queryStats">

<thead>
<tr><th colspan=8><B>Query stats</B></th></tr>
<%
{//code block
String strColumnHeaders =

"<tr><th>Query</th>"+
"<th>Execution Count</th>"+
"<th>Execution AvgTime</th>"+
"<th>Max Time</th>"+
"<th>Min Time</th>"+
"<th>Execution Row</th>"+
"<th>Cache Hits</th>"+
"<th>Cache Puts</th>"+
"<th>Cache Miss</th>"+
"</tr>";
%>

</thead>
<%
class ComprableQueryStatsContainer implements Comparable
{
public String query;
public QueryStatistics queryStatistics;
public int compareTo(Object object)
{
if(!(object instanceof ComprableQueryStatsContainer))
{
throw new RuntimeException("Not instance of QueryComparator");
}

QueryStatistics queryStatisticsObject =((ComprableQueryStatsContainer) object).queryStatistics;

long diff = queryStatistics.getExecutionAvgTime()*queryStatistics.getExecutionCount();
diff -= queryStatisticsObject.getExecutionAvgTime()*queryStatisticsObject.getExecutionCount();

if(diff==0)
{
diff = queryStatistics.getExecutionMaxTime()-queryStatisticsObject.getExecutionMaxTime();
}
return (int)(-diff);
}
}

TreeSet treeSet = new TreeSet();
String[] queries = statistics.getQueries();
for(int entityIndex=0;entityIndex<queries.length;entityIndex ++)
{
ComprableQueryStatsContainer comprableQueryStatsContainer = new ComprableQueryStatsContainer();
QueryStatistics queryStatistics =statistics.getQueryStatistics(queries[entityIndex]);
comprableQueryStatsContainer.queryStatistics = queryStatistics;
comprableQueryStatsContainer.query=queries[entityIndex];
treeSet.add(comprableQueryStatsContainer);
}

int index =0;
for(Iterator iterator = treeSet.iterator();iterator.hasNext();index++)
{
ComprableQueryStatsContainer comprableQueryStatsContainer = (ComprableQueryStatsContainer)iterator.next();
QueryStatistics queryStatistics =comprableQueryStatsContainer.queryStatistics;
String query = comprableQueryStatsContainer.query;

%>

<%if(index%15==0){%> <%=strColumnHeaders%><%}%>
<tr><td><%=query%></td>
<td><%=queryStatistics.getExecutionCount()%></td>
<td><%=queryStatistics.getExecutionAvgTime()%></td>
<td><%=queryStatistics.getExecutionMaxTime()%></td>
<td><%=queryStatistics.getExecutionMinTime()%></td>
<td><%=queryStatistics.getExecutionRowCount()%></td>

<td><%=queryStatistics.getCacheHitCount()%></td>
<td><%=queryStatistics.getCachePutCount()%></td>
<td><%=queryStatistics.getCacheMissCount()%></td>

</tr>
<%
}
}
%>
</table>

<br><br><br>


</body>
</html>