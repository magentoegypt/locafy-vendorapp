//
//  ced_membershipLabelValueStackTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 21/09/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_membershipLabelValueStackTableViewCell: UITableViewCell {

    //MARK:- Stored properties
    
    static var reuseID = "ced_membershipLabelValueStackTableViewCell"

    //MARK:- Init
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        initView()
    }
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    //MARK:- UIElement and helpers
    
    lazy var labelValStack:UIStackView = {
        let stack = UIStackView()
        stack.alignment = .fill
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        return stack
    }()
    lazy var leftLabel:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 16, weight: .semibold)
        //label.text = "Allowed Categories".localized
        return label
    }()
    lazy var rightLabel:UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15, weight: .regular)
        return label
    }()
    
    func initView(){
        addSubview(labelValStack)
        labelValStack.addSubViews([leftLabel,rightLabel], in: labelValStack)
        
        labelValStack.anchor(top: topAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 0, paddingLeft: 0, paddingBottom: 0, paddingRight: 0)
    }
    
    var leftLabelText:String?{
        didSet{
            leftLabel.text = leftLabelText
        }
    }
    var rightLabelText:String?{
        didSet{
            rightLabel.text = rightLabelText
        }
    }
}
