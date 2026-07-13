//
//  Util.swift
//  VenderApp
//
//  Created by Apple on 02/05/2024.
//  Copyright © 2024 CEDCOSS Technologies Private Limited. All rights reserved.
//

import Foundation

class Util{
    
    class func deactivateRTL(of view: UIView) {
        // 1. code here do something with view
        for subview in view.subviews {
            subview.semanticContentAttribute = .forceLeftToRight
            if let textField = subview as? UITextField{
                textField.textAlignment = .left
            }
            deactivateRTL(of: subview)
        }
    }
}
