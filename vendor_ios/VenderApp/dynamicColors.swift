//
//  dynamicColors.swift
//  MageNative Magento Platinum
//
//  Created by cedcoss on 11/01/20.
//  Copyright © 2020 CEDCOSS Technologies Private Limited. All rights reserved.
//

import Foundation


struct DynamicColor {

  static let darkThemeColor  = UIColor.init(hexString:  "#002f63")//"#283655")//"#4897D8")
  static let lightThemeColor =  UIColor.init(hexString: "#002f63")// "#283655")//"#4897D8")
  static var secondaryColor   = UIColor.init(hexString: "#002f63")!//"#92AAC7")!
    static var saveButtonColor: UIColor{
        return UIColor(hexString: "#F34A4A")!
    }
    static var themeColor: UIColor = {
        if #available(iOS 13.0, *) {
            return UIColor { (UITraitCollection) -> UIColor in
                if UITraitCollection.userInterfaceStyle == .dark { return darkThemeColor!}
                else { return lightThemeColor!}
            }
        }
        else { return lightThemeColor! }
    }()
  static var labelColor: UIColor = {
    if #available(iOS 13.0, *) { return UIColor.label }
    else { return .black }
  }()
  
  static var ViewBackgroundColor: UIColor = {
    if #available(iOS 13.0, *) { return .systemBackground}
    else { return .white}
  }()
  
    static var secondaryViewBackground: UIColor = {
      if #available(iOS 13.0, *) { return .secondarySystemBackground}
      else { return .darkGray}
    }()
    
    static var tertiaryViewBackground: UIColor = {
        if #available(iOS 13.0, *) { return .tertiarySystemBackground}
        else { return .lightGray}
      }()
    
    static var headingTextColor: UIColor = {
    if #available(iOS 13.0, *) { return UIColor.white }
    else { return .white}
    
  }()
  
  static var headingBackgroundColor: UIColor = {
    if #available(iOS 13.0, *) {
      return UIColor { (UITraitCollection) -> UIColor in
        if UITraitCollection.userInterfaceStyle == .dark { return UIColor.init(hexString: "#2980b9")! }
        else { return UIColor.init(hexString: "#2980b9")! }
      }
    } else {
      return UIColor.init(hexString: "#2980b9")!
    }
  }()
    static var tintColor: UIColor = {
        
        if #available(iOS 13.0, *) {
            
            return UIColor { (trait) -> UIColor in
                if trait.userInterfaceStyle == .dark { return secondaryColor}
                else { return secondaryColor}
            }
        } else { return secondaryColor}
        
    }()
    
}


