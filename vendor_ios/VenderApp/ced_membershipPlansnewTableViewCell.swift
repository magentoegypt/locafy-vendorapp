//
//  ced_membershipPlansnewTableViewCell.swift
//  VenderApp
//
//  Created by cedcoss on 14/09/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_membershipPlansnewTableViewCell: UITableViewCell {
    
    //MARK:- Stored properties
    static var reuseId:String = "ced_membershipPlansnewTableViewCell"
    var memberShipData = [[String:String]]()
    weak var parent: UIViewController?
    
    //MARK:- UI Elements
    lazy var PlansLabel:UILabel = {
        let lbl = UILabel()
        lbl.font = .systemFont(ofSize: 16, weight: .semibold)
        lbl.textColor = DynamicColor.headingTextColor
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.text = "  " + "Vendor Membership Plans".localized + "  "
        lbl.numberOfLines = 1
        lbl.layer.cornerRadius = 3.0
        return lbl
    }()
    
    lazy var membershipCollection:UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.minimumLineSpacing = 3
        layout.scrollDirection = .vertical
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.backgroundColor = UIColor.clear
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.register(ced_memberShipnewCollectionViewCell.self, forCellWithReuseIdentifier: ced_memberShipnewCollectionViewCell.reuseID)
        return collectionView
    }()

    //MARK:- Lifecycle
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        backgroundColor         = .clear
        selectionStyle          = .none
        contentView.isMultipleTouchEnabled  = true
        
        addSubview(PlansLabel)
        addSubview(membershipCollection)
        PlansLabel.anchor(top: topAnchor, left: leadingAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 10, paddingRight: 10, height: 40)
        membershipCollection.anchor(top: PlansLabel.bottomAnchor, left: leadingAnchor, bottom: bottomAnchor, right: trailingAnchor, paddingTop: 5, paddingLeft: 5, paddingBottom: 8, paddingRight: 5)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func populate(with data: [[String:String]], parent: UIViewController) {
        self.memberShipData   = data
        self.parent     = parent
        membershipCollection.delegate = self
        membershipCollection.dataSource = self
        membershipCollection.reloadData()
    }
}
//MARK:- Collection view delegates

extension ced_membershipPlansnewTableViewCell:UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout{
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.memberShipData.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ced_memberShipnewCollectionViewCell.reuseID, for: indexPath) as! ced_memberShipnewCollectionViewCell
        cell.parent = self.parent
        cell.populate(with: self.memberShipData[indexPath.item])
        cell.ContainerView.cardView()
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: UIScreen.main.bounds.width/2 - 25, height: 320)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 5
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 5
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let vc = ced_membershipDetailsViewController()
        vc.memberShipID = self.memberShipData[indexPath.item]["membership_id"] ?? ""
        vc.memberShipNameValue = self.memberShipData[indexPath.item]["membership_name"] ?? ""
        parent?.navigationController?.pushViewController(vc, animated: true)
    }

}
