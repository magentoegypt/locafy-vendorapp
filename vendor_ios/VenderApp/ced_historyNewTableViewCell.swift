//
//  ced_historyNewTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 14/09/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_historyNewTableViewCell : UITableViewCell {

    //MARK:- Stored properties
    
    static var reuseID = "ced_historyNewTableViewCell"
    weak var parent: UIViewController!
    var isFromMembersship = false

    //MARK:- Init
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        selectionStyle = .none
       // initViews()
    }
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    //MARK: - UI Controls Properties
    
    lazy var runningPlansLabel:UILabel = {
        let lbl = UILabel()
        lbl.font = .systemFont(ofSize: 16, weight: .semibold)
        lbl.textColor = DynamicColor.headingTextColor
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.text = "  " + "Vendor Running Plans".localized + "  "
        lbl.numberOfLines = 1
        lbl.layer.cornerRadius = 3.0
        return lbl
    }()

    lazy var ContainerView:UIView = {
        let view = UIView()
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        return view
    }()
    
    lazy var historyHorizontalStack1:UIStackView = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        stack.alignment = .fill
        stack.spacing = 0
        return stack
    }()
    lazy var historyHorizontalStack2:UIStackView = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        stack.alignment = .fill
        stack.spacing = 0
        return stack
    }()
    lazy var historyHorizontalStack3:UIStackView = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        stack.alignment = .fill
        stack.spacing = 0
        return stack
    }()
    lazy var historyHorizontalStack4:UIStackView = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        stack.alignment = .fill
        stack.spacing = 0
        return stack
    }()
    lazy var historyHorizontalStack5:UIStackView = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        stack.alignment = .fill
        stack.spacing = 0
        return stack
    }()
    lazy var historyHorizontalStack6:UIStackView = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        stack.alignment = .fill
        stack.spacing = 0
        return stack
    }()
    lazy var historyHorizontalStack7:UIStackView = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        stack.alignment = .fill
        stack.spacing = 0
        return stack
    }()
    lazy var historyHorizontalStack8:UIStackView = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.axis = .horizontal
        stack.alignment = .fill
        stack.spacing = 0
        return stack
    }()
    
    lazy var historyVerticalStack:UIStackView = {
        let stack = UIStackView()
        stack.distribution = .fillProportionally
        stack.axis = .vertical
        stack.alignment = .fill
        stack.spacing = 0
        return stack
    }()
    
    lazy var boldLabel1:UILabel = {
        let label = UILabel()
        label.textColor = DynamicColor.labelColor
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var normalLabel1:UILabel = {
        let label = UILabel()
        label.textColor = .lightGray
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var boldLabel2:UILabel = {
        let label = UILabel()
        label.textColor = DynamicColor.labelColor
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var normalLabel2:UILabel = {
        let label = UILabel()
        label.textColor = .lightGray
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var boldLabel3:UILabel = {
        let label = UILabel()
        label.textColor = DynamicColor.labelColor
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var normalLabel3:UILabel = {
        let label = UILabel()
        label.textColor = .lightGray
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var boldLabel4:UILabel = {
        let label = UILabel()
        label.textColor = DynamicColor.labelColor
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var normalLabel4:UILabel = {
        let label = UILabel()
        label.textColor = .lightGray
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var boldLabel5:UILabel = {
        let label = UILabel()
        label.textColor = DynamicColor.labelColor
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var normalLabel5:UILabel = {
        let label = UILabel()
        label.textColor = .lightGray
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var boldLabel6:UILabel = {
        let label = UILabel()
        label.textColor = DynamicColor.labelColor
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 2
        return label
    }()
    
    lazy var normalLabel6:UILabel = {
        let label = UILabel()
        label.textColor = .lightGray
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 2
        return label
    }()
    lazy var boldLabel7:UILabel = {
        let label = UILabel()
        label.textColor = DynamicColor.labelColor
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var normalLabel7:UILabel = {
        let label = UILabel()
        label.textColor = .lightGray
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var boldLabel8:UILabel = {
        let label = UILabel()
        label.textColor = DynamicColor.labelColor
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    lazy var normalLabel8:UILabel = {
        let label = UILabel()
        label.textColor = .lightGray
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        return label
    }()
    
    func initViews(){
        addSubview(runningPlansLabel)
        addSubview(ContainerView)
        ContainerView.addSubview(historyVerticalStack)
        constraintView()
    }
    
    func constraintView(){
        if isFromMembersship{
            runningPlansLabel.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 0, paddingLeft: 8, paddingRight: 8, height: 0)
        }else{
            runningPlansLabel.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 0, paddingLeft: 8, paddingRight: 8, height: 0)
        }
        ContainerView.anchor(top: runningPlansLabel.bottomAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 8, paddingLeft: 8, paddingBottom: 8, paddingRight: 8)
        historyVerticalStack.anchor(top: ContainerView.topAnchor, left: ContainerView.leadingAnchor, bottom: ContainerView.bottomAnchor, right: ContainerView.trailingAnchor, paddingTop: 0, paddingLeft: 5, paddingBottom: 0, paddingRight: 5)
        
        historyHorizontalStack1.addSubViews([boldLabel1,normalLabel1], in: historyHorizontalStack1)
        historyHorizontalStack2.addSubViews([boldLabel2,normalLabel2], in: historyHorizontalStack2)
        historyHorizontalStack3.addSubViews([boldLabel3,normalLabel3], in: historyHorizontalStack3)
        historyHorizontalStack4.addSubViews([boldLabel4,normalLabel4], in: historyHorizontalStack4)
        historyHorizontalStack5.addSubViews([boldLabel5,normalLabel5], in: historyHorizontalStack5)
        historyHorizontalStack6.addSubViews([boldLabel6,normalLabel6], in: historyHorizontalStack6)
        historyHorizontalStack7.addSubViews([boldLabel7,normalLabel7], in: historyHorizontalStack7)
        historyHorizontalStack8.addSubViews([boldLabel8,normalLabel8], in: historyHorizontalStack8)
        
        historyVerticalStack.addSubViews([historyHorizontalStack1,historyHorizontalStack2,historyHorizontalStack3,historyHorizontalStack4,historyHorizontalStack5,historyHorizontalStack6,historyHorizontalStack7,historyHorizontalStack8], in: historyVerticalStack)
    }
    
    var historyData : [String:String]?{
        didSet{
            boldLabel1.text = "ID".localized
            normalLabel1.text = historyData?["id"]
            
            boldLabel2.text = "Name".localized
            normalLabel2.text = historyData?["name"]
            
            boldLabel3.text = "Start Date".localized
            normalLabel3.text = historyData?["start_date"]
            
            boldLabel4.text = "Expire Date".localized
            normalLabel4.text = historyData?["expiry_date"]
            
            boldLabel5.text = "Order Id".localized
            normalLabel5.text = historyData?["order_id"]
            
            boldLabel6.text = "Payment Method".localized
            normalLabel6.text = historyData?["payment_name"]
            
            boldLabel7.text = "Status".localized
            normalLabel7.text = historyData?["status"]
            
            boldLabel8.text = "Transaction Id".localized
            normalLabel8.text = historyData?["transaction_id"]
            
            if isFromMembersship{
                normalLabel4.text = historyData?["expire_date"]
                normalLabel6.text = historyData?["payment_method"]
            }
        }
    }
    
}

extension UIStackView{
    func addSubViews(_ Views:[UIView],in Stack : UIStackView){
        for item in Views{
            Stack.addArrangedSubview(item)
        }
    }
}
