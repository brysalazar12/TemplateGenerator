# TemplateGenerator
Generate template file

Sample Template

**html.tpl**
```
Create basic html template. This is documentation of this template  
@start  
-f:footer  
-maincon:main_container  
-col1:left_column  
-col2:right_column  

@path: /path/where/file/will/generate/
@fileType:html
@filePrefix:
@fileSuffix:_template
@end
```  


```html
<html>
  <head>
    
  </head>
  <body>
    <div id="{main_container}">
      <div id="{left_column}">
      
      </div>
      <div id="{right_column}">
      
      </div>      
      <div id="{footer}">
      </div>      
    </div>
  </body>
</html>
```

**run:** java -jar path/to/TemplateGenerator.jar home -t=html -f=myfooter -maincon=main -col1=left -col2=right  
**output** /path/where/file/will/generate/home_template.html

```html
<html>
  <head>
    
  </head>
  <body>
    <div id="main">
      <div id="left">
      
      </div>
      <div id="right">
      
      </div>      
      <div id="myfooter">
      </div>      
    </div>
  </body>
</html>
```