//
//  ced_faqItemTableViewCell.swift
//  VenderApp
//
//  Created by Cedcoss on 24/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_faqItemTableViewCell: UITableViewCell {

    static let reuseId = "ced_faqItemTableViewCell"
    var parentController:ced_faqViewController?
    var faqId = ""
    
    lazy var container:UIView = {
        let view = UIView()
        view.clipsToBounds = true
        view.layer.borderWidth = 1.5
        view.layer.borderColor = DynamicColor.themeColor.cgColor
        view.layer.cornerRadius = 10.0
        view.backgroundColor = DynamicColor.tertiaryViewBackground
        return view
    }()
    
    lazy var topIdLabel:UILabel = {
        let lbl = UILabel()
        lbl.textColor = DynamicColor.ViewBackgroundColor
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.font = .systemFont(ofSize: 17, weight: .semibold)
        lbl.textAlignment = .center
        lbl.clipsToBounds = true
        lbl.layer.cornerRadius = 6.0
        return lbl
    }()
    
    lazy var btnLblView:UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        return view
    }()
    
    lazy var statusLabel:UILabel = {
        let lbl = UILabel()
        lbl.font = .systemFont(ofSize: 15, weight: .semibold)
        if(ced_storeVC.selectLangauge == "ar"){
            lbl.textAlignment = .right
        }
        return lbl
    }()
    
    lazy var editBtn:UIButton = {
        let btn = UIButton()
        btn.tintColor = DynamicColor.themeColor
        btn.setImage(UIImage(named: "edit")?.withRenderingMode(.alwaysTemplate), for: .normal)
        btn.addTarget(self, action: #selector(editButtontapped(_:)), for: .touchUpInside)
        return btn
    }()
    
    lazy var deleteBtn:UIButton = {
        let btn = UIButton()
        btn.tintColor = DynamicColor.themeColor
        btn.setImage(UIImage(named: "delete")?.withRenderingMode(.alwaysTemplate), for: .normal)
        btn.addTarget(self, action: #selector(deleteButtonTapped(_:)), for: .touchUpInside)
        return btn
    }()
    
    lazy var totalStack:UIStackView = {
        let stack = UIStackView()
        stack.axis = .vertical
        stack.spacing = 3.0
        return stack
    }()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        selectionStyle = .none
        contentView.isMultipleTouchEnabled = true
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func setupUI(){
        addSubview(container)
        container.anchor(top: topAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 25, paddingLeft: 0, paddingBottom: 5, paddingRight: 0)
        
        addSubview(topIdLabel)
        topIdLabel.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 40, paddingRight: 40, height: 40)
        
        container.addSubview(btnLblView)
        btnLblView.anchor(top: container.topAnchor, left: container.leadingAnchor, right: container.trailingAnchor, paddingTop: 20, paddingLeft: 0, paddingRight: 0, height: 35)
        
        btnLblView.addSubview(deleteBtn)
        deleteBtn.anchor(right: btnLblView.trailingAnchor, paddingRight: 20, width: 30, height: 30)
        deleteBtn.centerY(inView: btnLblView)
        
        btnLblView.addSubview(editBtn)
        editBtn.anchor(right: deleteBtn.leadingAnchor, paddingRight: 5, width: 30, height: 30)
        editBtn.centerY(inView: btnLblView)
        
        btnLblView.addSubview(statusLabel)
        statusLabel.anchor(left: btnLblView.leadingAnchor, right: editBtn.leadingAnchor, paddingLeft: 5, paddingRight: 5)
        statusLabel.centerY(inView: btnLblView)
        
        container.addSubview(totalStack)
        totalStack.anchor(top: btnLblView.bottomAnchor, left: container.leadingAnchor, bottom: container.bottomAnchor, right: container.trailingAnchor, paddingTop: 0, paddingLeft: 5, paddingBottom: 5, paddingRight: 5)
    }
    
    func populate(with faqData:[String:String]?){
        totalStack.subviews.forEach{$0.removeFromSuperview()}
        
        guard let faqData = faqData else {return}
        
        createRow(for: "Product ID".localized, value: faqData["product_id"])
        createRow(for: "Title".localized, value: faqData["title"])
       // createRow(for: "Customer Email".localized, value: faqData["email_id"])
        createRow(for: "Created At".localized, value: faqData["publish_date"])
        
        topIdLabel.text = "FAQ ID".localized + " : \(faqData["id"] ?? "")"
        self.faqId = faqData["id"] ?? ""
        
        let attributedStatusString = NSMutableAttributedString()
        
        if faqData["is_active"] == "0"{
            attributedStatusString.append(NSAttributedString(string: "Status".localized + " : ", attributes: [.font:UIFont.systemFont(ofSize: 15, weight: .semibold),.foregroundColor:DynamicColor.labelColor]))
            attributedStatusString.append(NSAttributedString(string: "Disabled".localized, attributes: [.font:UIFont.systemFont(ofSize: 15, weight: .medium),.foregroundColor:UIColor(hexString: "#FE3C00")]))
        }else{
            attributedStatusString.append(NSAttributedString(string: "Status".localized + " : ", attributes: [.font:UIFont.systemFont(ofSize: 15, weight: .semibold),.foregroundColor:DynamicColor.labelColor]))
            attributedStatusString.append(NSAttributedString(string: "Enabled".localized, attributes: [.font:UIFont.systemFont(ofSize: 15, weight: .medium),.foregroundColor:UIColor(hexString: "#3BAE55")]))
        }
        statusLabel.attributedText = attributedStatusString
    }
    
    
    
    private func createRow(for heading: String?, value: String?) {
        let label = UILabel()
        label.text = heading
        label.font = .systemFont(ofSize: 15, weight: .semibold)
        label.numberOfLines = 0
        
        label.anchor(width:140)
        
        let labelTwo = UILabel()
        labelTwo.text = value
        labelTwo.numberOfLines = 0
        labelTwo.font = .systemFont(ofSize: 15, weight: .regular)
        labelTwo.textColor = DynamicColor.labelColor
        
        let stack = UIStackView(arrangedSubviews: [label,labelTwo])
        stack.axis = .horizontal
//        stack.distribution = .fillEqually
        stack.spacing = 8.0
//        stack.anchor(height: 18)
        
        totalStack.addArrangedSubview(stack)
    }
    
    @objc func editButtontapped(_ sender:UIButton){
        guard let parentController = parentController else {
            return
        }
        let vc = ced_addFaqViewController()
        if faqId != ""{
            vc.faq_id = faqId
            parentController.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    @objc func deleteButtonTapped(_ sender:UIButton){
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        
        if faqId != ""{
            parentController?.sendRequest(url: "vfaqapi/deletefaq", params: ["vendor_id":vendorId,"faq_id":faqId])
        }
    }
}
