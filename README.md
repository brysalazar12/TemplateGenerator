# TemplateGenerator
Generate template file

Sample Template

###html.tpl

Create basic html template
```
**@start**  
**f**:footer  
**maincon**:main_container  
**col1**:left_column  
**col2**:right_column  
**@end**
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
