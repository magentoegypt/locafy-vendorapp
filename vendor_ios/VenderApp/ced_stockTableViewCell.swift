//
//  ced_LowStockTableViewCell.swift
//  VenderApp
//
//  Created by Cedcoss on 30/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_stockTableViewCell: UITableViewCell {

    static let reuseId = "ced_stockTableViewCell"
    var parentController:UIViewController?
    var prodId = ""
    
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
    
    lazy var editBtn:UIButton = {
        let btn = UIButton()
        btn.tintColor = DynamicColor.themeColor
        btn.setImage(UIImage(named: "edit")?.withRenderingMode(.alwaysTemplate), for: .normal)
        btn.addTarget(self, action: #selector(editButtontapped(_:)), for: .touchUpInside)
        return btn
    }()

    lazy var totalStack:UIStackView = {
        let stack = UIStackView()
        stack.axis = .vertical
        stack.spacing = 5.0
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
        
        btnLblView.addSubview(editBtn)
        editBtn.anchor(right: btnLblView.trailingAnchor, paddingRight: 20, width: 30, height: 30)
        editBtn.centerY(inView: btnLblView)
        
        container.addSubview(totalStack)
        totalStack.anchor(top: btnLblView.bottomAnchor, left: container.leadingAnchor, bottom: container.bottomAnchor, right: container.trailingAnchor, paddingTop: 0, paddingLeft: 5, paddingBottom: 5, paddingRight: 5)
    }
    
    func populate(with prodData:[String:String]?){
        totalStack.subviews.forEach{$0.removeFromSuperview()}
        
        guard let prodData = prodData else {return}
        
        createRow(for: "Product Name".localized, value: prodData["name"])
      //  createRow(for: "Type".localized, value: prodData["type"])
        createRow(for: "Price".localized, value: prodData["vendor_price"])
        createRow(for: "Quantity".localized, value: prodData["qty"])
        
        topIdLabel.text = "Product ID".localized + " : \(prodData["product_id"] ?? "")"
        self.prodId = prodData["product_id"] ?? ""
    }
    
    func populateData(with outStockprodData:[String:String]?){
        totalStack.subviews.forEach{$0.removeFromSuperview()}
        
        guard let prodData = outStockprodData else {return}
        
        createRow(for: "Product Name".localized, value: prodData["product_name"])
      //  createRow(for: "Type".localized, value: prodData["product_type"])
        createRow(for: "Price".localized, value: prodData["vendor_price"])
        createRow(for: "Quantity".localized, value: prodData["quantity"])
        
        topIdLabel.text = "Product ID".localized + " : \(prodData["product_id"] ?? "")"
        self.prodId = prodData["product_id"] ?? ""
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
        if let parentController = parentController as? ced_outOfStockInventoryViewController {
            Alert_File.addLoadingIndicator(parentController, msg: "Loading Please Wait...".localized)
            
            let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary;
            let vendor_id = userData["vendorId"] as! String;
            let hashkey    = userData["hashKey"] as! String;
            let storeId = UserDefaults.standard.value(forKey: "storeId") as? String ?? ""
            
            var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
            postString += "&product_id="+prodId
            postString += "&store_id=" + storeId
            
            print(postString);
            parentController.productId = prodId
            parentController.sendRequest(url: "vproductapi/vproducts/info/", parameters: postString);
        }
        if let parentController = parentController as? ced_lowStockInventoryViewController {
            Alert_File.addLoadingIndicator(parentController, msg: "Loading Please Wait...".localized)
            
            let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary;
            let vendor_id = userData["vendorId"] as! String;
            let hashkey    = userData["hashKey"] as! String;
            
            var postString = "vendor_id="+vendor_id+"&hashkey="+hashkey;
            postString += "&product_id="+prodId;
            
            print(postString);
            parentController.productId = prodId
            parentController.sendRequest(url: "vproductapi/vproducts/info/", parameters: postString);
        }
    }

}
