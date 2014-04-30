<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
    <head>
            <title>Index of ${directoryname}</title>
    </head>
    <body>
        <h1>Index of ${directoryname}</h1>
        <hr/>
        <pre>
        <#list files as file>
<a href="workingdirectory.action?path=${file}">${file}</a>
        </#list>
</pre>

        </pre>
        <hr/>
        <address style="font-size:small;">Propeller Web Loader: Working directory listing</address>
    </body>
</html>