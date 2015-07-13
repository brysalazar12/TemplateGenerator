This template is use to create controller
@start

cls:clsName|doc:Name of model
id:id|doc:Primary key -id:user_id
pup:publicProperties|doc:List of public properties. Ex: -pup=[firstName,lastName,age,location]
prp:privateProperties|doc:List of private properties.

@path:
@fileType:php
@filePrefix:
@fileSuffix:Controller
@end

<?php

class {fileName}Controller extends Controller {

	/**
	* Primary key
	*/
	public ${id}

	public ${publicProperties}

	private ${privateProperties}

	public function rules()
	{
		return [];
	}
}
