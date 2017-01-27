<!DOCTYPE HTML>
<!--  %@ include file="/WEB-INF/template/includes.jsp" %-->
 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
 
<html>
    <head>
        <meta name="google-site-verification" content="xxxxxxxxx" />
        <title><tiles:insertAttribute name="title" ignore="true"/></title>
        <meta name="description" content="<tiles:insertAttribute name="page_description" ignore="true"/>">
        <c:url var="cssURL" value="/static/css/style.min.css"/>
        <link href="${cssURL}" rel="stylesheet" type="text/css"/>
        <link rel="icon" href="<c:url value="/static/images/favicon.ico"/>" type="image/x-icon" />
        <link rel="shortcut icon" href="<c:url value="/static/images/favicon.ico"/>" type="image/x-icon" />
        <meta charset="utf-8">
        <meta property="og:image" content="<tiles:insertAttribute name="og_image" ignore="true"/>" />
        <meta property="og:title" content="<tiles:insertAttribute name="og_title" ignore="true"/>" />
        <meta property="og:description" content="<tiles:insertAttribute name="og_desc" ignore="true"/>"/>
        <link rel="stylesheet" href="<tiles:insertAttribute name="jquery_ui_css" ignore="true"/>" />
    </head>
    <body>
        <div id="banner">
            <tiles:insertAttribute name="header" />
        </div>
        <div></div>
        <tiles:insertAttribute name="navigation_bar" />
        <div></div>
        <div id="page">
            <tiles:insertAttribute name="content" />
        </div>
        <div></div>
        <div id="footer_wrapper">
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>