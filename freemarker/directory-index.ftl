<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
    <head>
            <title>Index of ${directoryname}</title>
    </head>
    <body>
        <h1>Index of ${directoryname}</h1>
        <hr/>
        <#if listingsuccess>
        <pre>
<#if showparentdir><a href="workingdirectory.action?path=${parentdir}">..</a></#if>
        <#list files as file>
<a href="workingdirectory.action?path=${directoryname}${file.getName()}">${file.getName()}<#if file.isDirectory()>/</#if></a>
</#list>
</pre>
        <#else>
            <p style="color:red;">Unable to create directory listing</p>
        </#if>
        <hr/>
        <address style="font-size:small;">Propeller Web Loader: Working directory listing</address>
    </body>
</html>