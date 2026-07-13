//
//  extensions.swift
//  VenderApp
//
//  Created by cedcoss on 28/05/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import Foundation
extension String {
    var localized: String {
       
        let path = Bundle.main.path(forResource: ced_storeVC.selectLangauge, ofType: "lproj");
//       print(path)
        let bundle = Bundle(path: path!);
   // print(bundle)
        return NSLocalizedString(self, tableName: nil, bundle: bundle!, value: "", comment: "");
    }
    
    func localized(forLanguageCode lanCode: String) -> String {
            guard
                let bundlePath = Bundle.main.path(forResource: lanCode, ofType: "lproj"),
                let bundle = Bundle(path: bundlePath)
            else { return "" }
            
            return NSLocalizedString(
                self,
                bundle: bundle,
                value: " ",
                comment: ""
            )
        }
    
    func dateConvertToCurrentTimezone(_ dateFormat:String)-> String{
        let english_dateFormatter = DateFormatter()
        let arabic_dateFormatter = DateFormatter()
        // Set the locale to Arabic (you can also use a specific Arabic locale like "ar_SA" for Saudi Arabia)
        english_dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        arabic_dateFormatter.locale = Locale(identifier: "ar")
        // Set the expected date format based on the string you have
        english_dateFormatter.dateFormat = dateFormat
        english_dateFormatter.timeZone = TimeZone(identifier: "UTC")
        arabic_dateFormatter.dateFormat = dateFormat
        arabic_dateFormatter.timeZone = TimeZone(identifier: "UTC")
        if let date = arabic_dateFormatter.date(from: self) {
            arabic_dateFormatter.timeZone = TimeZone.current//(identifier: "Africa/Cairo")
            return arabic_dateFormatter.string(from: date)
        }else if let date = english_dateFormatter.date(from: self) {
           // english_dateFormatter.timeZone = .current//TimeZone(identifier: "UTC")
            english_dateFormatter.timeZone = TimeZone.current//(identifier: "Africa/Cairo")
            return english_dateFormatter.string(from: date)
        }
        return self
    }
    
    func dateConvertToCurrentDatezone(_ dateFormat:String)-> String{
        let english_dateFormatter = DateFormatter()
        let arabic_dateFormatter = DateFormatter()
        // Set the locale to Arabic (you can also use a specific Arabic locale like "ar_SA" for Saudi Arabia)
        english_dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        arabic_dateFormatter.locale = Locale(identifier: "ar")
        // Set the expected date format based on the string you have
        english_dateFormatter.dateFormat = dateFormat
        english_dateFormatter.timeZone = TimeZone(identifier: "UTC")
        arabic_dateFormatter.dateFormat = dateFormat
        arabic_dateFormatter.timeZone = TimeZone(identifier: "UTC")
        if let date = arabic_dateFormatter.date(from: self) {
            arabic_dateFormatter.dateFormat = "yyyy-MM-dd"
            return arabic_dateFormatter.string(from: date)
        }else if let date = english_dateFormatter.date(from: self) {
            english_dateFormatter.dateFormat = "yyyy-MM-dd"
            return english_dateFormatter.string(from: date)
        }
        return self
    }
}
