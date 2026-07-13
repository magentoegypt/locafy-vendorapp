//
//  appSettings.swift
//  VenderApp
//
//  Created by Cedcoss on 28/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//"https://demo2.cedcommerce.com/magento/client/hassan/pub/"
//"https://brassandwood.net/"

import Foundation

struct settings{
    static var baseUrl: String {
        var url = "https://locafy.market/"//Ced_CommonVendor.getInfoPlist("baseUrl") as? String{"http://brassandwood.org/"//live
            url     +=   UserDefaults.standard.value(forKey: "storeCode") as? String ?? ""
            return url
    }
}
