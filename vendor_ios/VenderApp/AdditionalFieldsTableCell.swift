//
//  AdditionalFieldsTableCellTableViewCell.swift
//  VenderApp
//
//  Created by MacMini on 16/02/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class AdditionalFieldsTableCell: UITableViewCell,UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout {

    
    @IBOutlet weak var collectionView: UICollectionView!
    
    
    var store_fields=[[String:JSON]]()
    var business_fields=[[String:JSON]]()
    var bank_fields=[[String:JSON]]()
    var dataForAdditionalFields=[[String:String]]()
    var dataForFields=[[String:JSON]]()
    var business_type=[[String:JSON]]()
    var address_proof=[[String:JSON]]()
    
    var parent:UIViewController!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    @objc func loadView(){
        dataForAdditionalFields=[["heading":"Business Details!".localized,"desc":"You need to provide your GSTIN, TAN and other business information.".localized,"img":"stats"],["heading":"Bank Details!".localized,"desc":"We need your bank account details and KYC documents to verify your bank account.".localized,"img":"college"],["heading":"Store Details!".localized,"desc":"Your store details have been successfully verified.".localized,"img":"store"]]
        
        collectionView.dataSource=self
        collectionView.delegate=self
    }
    @objc func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    @objc func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return dataForAdditionalFields.count
    }
    @objc func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell=collectionView.dequeueReusableCell(withReuseIdentifier: "AdditionalFieldsCollectionCell", for: indexPath) as! AdditionalFieldsCollectionCell
        cell.headLabel.text=dataForAdditionalFields[indexPath.row]["heading"]
        cell.detailLabel.text=dataForAdditionalFields[indexPath.row]["desc"]
        cell.imgView.image=UIImage(named: dataForAdditionalFields[indexPath.row]["img"]!)?.withRenderingMode(.alwaysTemplate)
        cell.imgView.contentMode = .scaleAspectFit
        cell.imgView.tintColor = DynamicColor.labelColor
        cell.addDetailsButton.setTitle("Add Details".localized, for: .normal)
        cell.addDetailsButton.tag=indexPath.row
        cell.addDetailsButton.addTarget(self, action: #selector(addDetailsButtonPressed(_:)), for: .touchUpInside)
        cell.viewForCard.cardView()
        return cell
    }

    @objc func addDetailsButtonPressed(_ sender: UIButton){
        
        let vc=UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "AdditionalFieldsViewController") as! AdditionalFieldsViewController
        
        if dataForAdditionalFields[sender.tag]["heading"]=="Business Details!".localized{
            vc.topLabelText="Business Details!".localized
            //vc.viewsData=business_fields
            
        }else if dataForAdditionalFields[sender.tag]["heading"]=="Bank Details!".localized{
            vc.topLabelText="Bank Details!".localized
            //vc.viewsData=bank_fields
            //vc.business_type=business_type
            //vc.address_proof=address_proof
        }else if dataForAdditionalFields[sender.tag]["heading"]=="Store Details!".localized{
            vc.topLabelText="Store Details!".localized
            //vc.viewsData=store_fields
        }
        parent.navigationController?.pushViewController(vc, animated: true)
        
        
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: self.collectionView.frame.width-5, height: self.collectionView.frame.height-5)
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
