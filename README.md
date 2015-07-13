# TemplateGenerator
Generate template file

##Sample Template

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
**output:** /path/where/file/will/generate/home_template.html  
Content of generated **home_template.html**
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

##Sample Template
**controller.tpl**
```
This template is use to create controller  
@start  
-id:id|doc:Primary key. Ex. -id=user_id  
-pup:publicProperties|doc:List of public properties. Ex. -pup=[firstName,lastName,age,location]  
-prp:privateProperties|doc:List of private properties  

@path  
@fileType:php  
@filePrefix:  
@fileSuffix:Controller  
@end  
<?php

class {fileName}Controller extends Controller {
	/**
	 * Primary key
	 */

	public ${id};
	
	public ${publicProperties};

	private ${privateProperties};

	public function __construct() {

	}
}
```

**run:** java -jar path/to/TemplateGenerator.jar User -t=controller -id=user_id -pup=[firstName,lastName,age,location]  
**output:** /path/where/file/will/generate/UserController.php  

```php
<?php

class UserController extends Controller {
	/**
	 * Primary key
	 */

	public $user;
	
	public $firstName;
	public $lastName;
	public $age;
	public $location;

	private ${privateProperties};

	public function __construct() {

	}
}
```