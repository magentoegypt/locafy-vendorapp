//
//  optionItemView.swift
//  VenderApp
//
//  Created by Cedcoss on 06/10/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class optionItemView: UIView {

    var isDefault = false
    
    var optionID = ""
    
    lazy var scrollOption: UIScrollView = {
        UIScrollView()
    }()
    lazy var hStack: UIStackView = {
        let stack = UIStackView()
        stack.axis = .horizontal
        stack.distribution = .fill
        stack.spacing = 10
        return stack
    }()
    lazy var deleteBtn : UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "delete"), for: .normal)
        btn.tintColor = DynamicColor.themeColor
//        btn.addTarget(self, action: #selector(deleteTapped), for: .touchUpInside)
        return btn
    }()
    lazy var isDefaultBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("is Default", for: .normal)
        btn.setTitleColor(DynamicColor.themeColor, for: .normal)
//        let image: UIImage? = isDefault ? UIImage(named: "checked") : UIImage(named: "unchecked")
//        btn.setImage(image, for: .normal)
        btn.setImage(UIImage(named: "unchecked"), for: .normal)
        btn.tintColor = DynamicColor.themeColor
        btn.contentHorizontalAlignment = .leading
        btn.titleEdgeInsets = .init(top: 0, left: 8, bottom: 0, right: 0)
//        btn.addTarget(self, action: #selector(newsletterTapped), for: .touchUpInside)
        return btn
    }()

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func setupViews(){
        addSubview(isDefaultBtn)
        isDefaultBtn.anchor(top: topAnchor, left: leadingAnchor, paddingTop: 0, paddingLeft: 8,width: 150, height: 35)
        
        addSubview(deleteBtn)
        deleteBtn.anchor(top: topAnchor, right: trailingAnchor, paddingTop: 0, paddingRight: 8, width: 30, height: 35)
        
        addSubview(scrollOption)
        scrollOption.anchor(top: isDefaultBtn.bottomAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 0, paddingBottom: 0, paddingRight: 0)
        
        scrollOption.addSubview(hStack)
        hStack.anchor(top: scrollOption.topAnchor, left: scrollOption.leadingAnchor, bottom: scrollOption.bottomAnchor, right: scrollOption.trailingAnchor, paddingTop: 5, paddingLeft: 0, paddingBottom: 0, paddingRight: 0,height: 60)
    }
    
    @objc private func newsletterTapped() {
        isDefault.toggle()
        let image: UIImage? = isDefault ? UIImage(named: "checked") : UIImage(named: "unchecked")
        isDefaultBtn.setImage(image, for: .normal)
    }
    
//    @objc func deleteTapped(){
//        removeFromSuperview()
//    }
}
