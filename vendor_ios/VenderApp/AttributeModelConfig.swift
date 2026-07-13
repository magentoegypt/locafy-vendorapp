//
//  AttributeModelConfig.swift
//  LocafyApp
//
//  Created by Apple on 02/04/2025.
//  Copyright © 2025 CEDCOSS Technologies Private Limited. All rights reserved.
//

class AttributeModelConfig {
  var assicoiatProduct_id:String?
  var assicoiatProduct_sku:String?
  var title: String?
  var attribute_Id: String?
  var attribute_Label: String?
  var attribute_code: String?
  var isChecked:Bool = false
  var isUserCustom:Bool = false
  var isEnable:Bool = true
  var attributeArray:[AttributeModelConfig] = [AttributeModelConfig]()
  var thirdtabImageOption:String = "Skip"
  var thirdtabPriceOption:String = "Skip"
  var thirdtabQuantityOption:String = "Skip"
  var thirdtabImageAttributeIndex:Int = 0
  var thirdtabPriceAttributeIndex:Int = 0
  var thirdtabQuantityAttributeIndex:Int = 0
  var attributeWithValue: String?
  var attributeIdsArr: [String] = [String]()
  var namelabel: String?
  var skulabel: String?
  var label: String?
  var value: String?
  var image:UIImage?
  var imageUrl:String?
  var defaultimage:String?
  var oldweight:String?
  var oldprice:String?
  var weight:String?
  var price:String?
  var quantity:String?
}
