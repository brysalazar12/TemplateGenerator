Create simple class that extends the Model class
@start

cls:clsName,doc:Name of model
id:id,doc:Primary key -id:user_id
pup:publicProperties,doc:List of public properties. Ex: -pup=[firstName,lastName,age,location]
prp:privateProperties,doc:List of private properties.

@end

<?php

class {clsName} extends Model {

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
