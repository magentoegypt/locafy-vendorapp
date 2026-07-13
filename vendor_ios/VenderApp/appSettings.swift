//
//  appSettings.swift
//  VenderApp
//
//  Created by Cedcoss on 28/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.

import Foundation

struct settings{
    static var baseUrl: String {
        var url = "https://vendors.magento2.click/"
            url     +=   UserDefaults.standard.value(forKey: "storeCode") as? String ?? ""
            return url
    }
}
