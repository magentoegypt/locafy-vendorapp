//
//  optionsView.swift
//  VenderApp
//
//  Created by Cedcoss on 06/10/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class optionsView: UIView {

    lazy var topLabel:UILabel = {
        let label = UILabel()
        label.text = "Manage Options"
        label.textAlignment = .center
        label.backgroundColor = DynamicColor.themeColor
        label.textColor = .white
        label.font = .systemFont(ofSize: 16, weight: .semibold)
        label.clipsToBounds = true
        label.layer.cornerRadius = 6.0
        return label
    }()
    
    lazy var addButton:UIButton = {
        let btn = UIButton()
        btn.setTitle("+Add", for: .normal)
        btn.titleLabel?.font = .systemFont(ofSize: 15, weight: .semibold)
        btn.setTitleColor(DynamicColor.themeColor, for: .normal)
        return btn
    }()
    
    lazy var optionStack:UIStackView = {
        let stack = UIStackView()
        stack.alignment = .fill
        stack.distribution = .fill
        stack.spacing = 5
        stack.axis = .vertical
        return stack
    }()

    override init(frame: CGRect) {
        super.init(frame: frame)
        configureViews()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func configureViews(){
        addSubview(addButton)
        addButton.anchor(top: topAnchor, right: trailingAnchor, paddingTop: 0, paddingRight: 8, width: 80, height: 35)
        
        addSubview(topLabel)
        topLabel.anchor(top: topAnchor, left: leadingAnchor,right: addButton.leadingAnchor, paddingTop: 0, paddingLeft: 8, paddingRight: 8, height: 35)
        
        addSubview(optionStack)
        optionStack.anchor(top: topLabel.bottomAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 5)
    }
}
