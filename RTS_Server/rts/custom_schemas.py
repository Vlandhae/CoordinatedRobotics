from rest_framework.schemas.openapi import AutoSchema
class RegisterCarSchema(AutoSchema):
    def get_components(self, path, method):
        return super().get_components(path, method)
    
    def get_operation(self, path, method):
        data = super().get_operation(path, method)                
        responses = {
            '201': {
                'content': {
                    "text/plain" : {
                        "schema":{
                            "type":"integer",
                            "format":"int32"
                        },
                        "example":"5"
                    }
                },
                'description': 'The id of the newly registered car in plaintext'
            }
        }
        data["responses"] = responses        
        return data


class EditCarSessionSchema(AutoSchema):
    def get_components(self, path, method):
        return super().get_components(path, method)
    
    def get_operation(self, path, method):
        data = super().get_operation(path, method)        
        # OpenAPI documentation for the request body
        newBody ={
            "content": {
                "text/plain": {
                    "schema": {
                        "type": "integer",
                        "format":"int32",
                        "required": "true",
                        "description": "testDescription",
                        "example" : "5"
                    },
                    "description":"testDesc"
                },
                "application/json":{
                    "schema": {
                        "type":"object",
                        "required":["car"],
                        "properties":{
                            "car": {
                                "type":"integer",
                                "format":"int32"                                
                            }
                        }
                    }
                }
            }
        }
        # insert the body documentation
        data["requestBody"] = newBody        
        return data
