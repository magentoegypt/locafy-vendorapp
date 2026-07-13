//
//  optionfieldView.swift
//  VenderApp
//
//  Created by Cedcoss on 06/10/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class optionfieldView: UIView {

    lazy var optionField:UITextField = {
        let field = UITextField()
        field.borderStyle = .roundedRect
        field.backgroundColor = DynamicColor.secondaryViewBackground
        field.textAlignment = .center
        field.font = .systemFont(ofSize: 13, weight: .regular)
        return field
    }()
    
    lazy var optionLabel:UILabel = {
        let lbl = UILabel()
        lbl.font = .systemFont(ofSize: 13, weight: .semibold)
        lbl.textAlignment = .center
        lbl.text = "test"
        return lbl
    }()
    
    var key = ""
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        configView()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func configView(){
        addSubview(optionLabel)
        optionLabel.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 0, paddingLeft: 0, paddingRight: 0, height: 25)
        
        addSubview(optionField)
        optionField.anchor(top: optionLabel.bottomAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 0, paddingLeft: 0, paddingBottom: 0, paddingRight: 0)
    }
}
