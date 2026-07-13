//
//  ced_membershipAllowedCategoryTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 21/09/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_membershipAllowedCategoryTableViewCell: UITableViewCell {

    //MARK:- Stored properties
    
    static var reuseID = "ced_membershipAllowedCategoryTableViewCell"

    //MARK:- Init
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        initView()
    }
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    //MARK:- UI Elements
    
    lazy var topLabel:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 16, weight: .medium)
        label.text = "Allowed Categories".localized
        return label
    }()
    lazy var categoryStack:UIStackView = {
        let stack = UIStackView()
        stack.alignment = .fill
        stack.axis = .vertical
        stack.distribution = .fillEqually
        return stack
    }()
//    lazy var stackItem:UILabel = {
//
//        return label
//    }()
//
    //MARK:- UIHelpers
    
    func initView(){
        addSubview(topLabel)
        addSubview(categoryStack)
        topLabel.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 0, paddingLeft: 5, paddingRight: 5, height: 30)
        categoryStack.anchor(top: topLabel.bottomAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 0, paddingLeft: 15, paddingBottom: 0, paddingRight: 15)
    }
    
    var categoryItems:[String]?{
        didSet{
            self.categoryStack.subviews.forEach{$0.removeFromSuperview()}
            categoryItems?.forEach {item in
                let label = UILabel()
                label.font = .systemFont(ofSize: 15, weight: .regular)
                label.text = item
                self.categoryStack.addArrangedSubview(label)
            }
        }
    }
}
