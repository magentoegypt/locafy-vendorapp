//
//  ced_memberShipnewCollectionViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 14/09/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_memberShipnewCollectionViewCell: UICollectionViewCell {
    
    //MARK:- stored properties
    
    static var reuseID = "ced_memberShipnewCollectionViewCell"
    var memberShipItem = [String:String]()
    var parent : UIViewController?
    
    //MARK:- UI elements
    
    lazy var ContainerView:UIView = {
        let view = UIView()
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        return view
    }()
    
    lazy var memberShipName:UILabel = {
        let label = UILabel()
        label.textColor = DynamicColor.labelColor
        label.numberOfLines = 2
        label.font = .systemFont(ofSize: 15, weight: .medium)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    lazy var memberShipImage:UIImageView = {
        let imageView = UIImageView()
        imageView.clipsToBounds = true
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    
    lazy var memberShipPrice:UILabel = {
        let label = UILabel()
        label.textColor = DynamicColor.labelColor
        label.numberOfLines = 1
        label.font = .systemFont(ofSize: 14, weight: .semibold)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    lazy var specialPrice:UILabel = {
        let label = UILabel()
      //  label.textColor = UIColor.init(hexString: "#ff0000")
        label.numberOfLines = 1
       //label.font = .systemFont(ofSize: 15, weight: .semibold)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    lazy var AddToCart:UIButton = {
        let label = UIButton(type: .system)
        label.backgroundColor = DynamicColor.themeColor
        label.setTitleColor( DynamicColor.headingTextColor, for: .normal)
        label.titleLabel?.font = .systemFont(ofSize: 15, weight: .semibold)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.isEnabled = true
        return label
    }()
    
    
    //MARK:- Lifecycle

    override init(frame: CGRect) {
        super.init(frame: frame)
        
        contentView.isMultipleTouchEnabled = true
        commonInit()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func commonInit(){
        addSubview(ContainerView)
        ContainerView.addSubview(memberShipImage)
        ContainerView.addSubview(memberShipName)
        ContainerView.addSubview(memberShipPrice)
        ContainerView.addSubview(specialPrice)
        ContainerView.addSubview(AddToCart)
        
        ContainerView.anchor(top: topAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingBottom: 5, paddingRight: 5)
        memberShipImage.anchor(top: ContainerView.topAnchor, left: ContainerView.leadingAnchor, right: ContainerView.trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingRight: 5,height: 150)
        memberShipName.anchor(top: memberShipImage.bottomAnchor, left: ContainerView.leadingAnchor , right: ContainerView.trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingRight: 5,height: 50)
        memberShipPrice.anchor(top: memberShipName.bottomAnchor, left: ContainerView.leadingAnchor, right: ContainerView.trailingAnchor, paddingTop: 0, paddingLeft: 5, paddingRight: 5,height: 25)
        specialPrice.anchor(top: memberShipPrice.bottomAnchor, left: ContainerView.leadingAnchor, right: ContainerView.trailingAnchor, paddingTop: 0, paddingLeft: 5, paddingRight: 5,height: 25)
        AddToCart.anchor(top: specialPrice.bottomAnchor, left: ContainerView.leadingAnchor, right: ContainerView.trailingAnchor, paddingTop: 0, paddingLeft: 15, paddingRight: 15,height: 40)
    }
    
    func populate(with data: [String:String]) {
        self.memberShipItem = data
        self.memberShipImage.sd_setImage(with: URL(string: memberShipItem["membership_image"] ?? ""), placeholderImage: UIImage(named: "placeholder"))
        self.memberShipName.text = memberShipItem["membership_name"] ?? ""
        self.memberShipPrice.text = memberShipItem["price"] ?? ""
        if let _ = memberShipItem["already_subscribed"] {
            AddToCart.isEnabled = false
            AddToCart.setTitle("Already Subscribed".localized, for: .normal)
        }
        if let _ = memberShipItem["show_addtocart"]{
            AddToCart.isEnabled = true
            AddToCart.setTitle("Add to Cart".localized, for: .normal)
        }
        
        if let special = memberShipItem["special_price"]{
            specialPrice.isHidden = false
            let finalStr = NSMutableAttributedString()
            let attributes1: [NSAttributedString.Key: Any] = [
                .foregroundColor: UIColor.red,
                .font: UIFont.systemFont(ofSize: 12, weight: .medium)
            ]
            let initialString = NSMutableAttributedString(string: "Special Price:".localized, attributes: attributes1)
            finalStr.append(initialString)
            let attributes2: [NSAttributedString.Key: Any] = [
                .foregroundColor: DynamicColor.labelColor,
                .font: UIFont.systemFont(ofSize: 13, weight: .semibold)
            ]
            let otherString = NSMutableAttributedString(string: special, attributes: attributes2)
            finalStr.append(otherString)
            specialPrice.attributedText = finalStr
        }else{
            specialPrice.isHidden = true
        }
      //  AddToCart.tag = Int(memberShipItem["membership_id"] ?? "0") ?? 0
        AddToCart.addTarget(self, action: #selector(addToCartTapped(_:)), for: .touchUpInside)
 
    }
    @objc func addToCartTapped(_ sender : UIButton){
        print("in here too")
        Alert_File.addLoadingIndicator(parent ?? UIViewController(), msg: "Loading Please Wait...")
        let id = memberShipItem["membership_id"]
        if let parent = parent as? ced_membershipPlanNewViewController{
            print("in here")
            let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
         //   let hashKey    = userData["hashKey"] as! String
            var params = [String:String]()
            params["vendor_id"] = vendorId
            params["membership_id"] = id
            parent.sendRestFullRequest(url: "addMembership", params: params, store: false)
        }
    }
    
}
