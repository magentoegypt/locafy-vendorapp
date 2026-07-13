/**
* CedCommerce
*
* NOTICE OF LICENSE
*
* This source file is subject to the End User License Agreement (EULA)
* that is bundled with this package in the file LICENSE.txt.
* It is also available through the world-wide-web at this URL:
* http://cedcommerce.com/license-agreement.txt
*
* @category  Ced
* @package   MageNative MultiVendor
* @author    CedCommerce Core Team <connect@cedcommerce.com >
* @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
* @license      http://cedcommerce.com/license-agreement.txt
*/

import UIKit

class ced_vendorDashboardtopCell: UITableViewCell,UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout,UIScrollViewDelegate {

    @IBOutlet weak var colorViewHeight: NSLayoutConstraint!
    @IBOutlet weak var pages: UIPageControl!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var pageControl: UIView!
    var slideData = [String:String]()
    var parent = UIViewController()
    let defaults = UserDefaults.standard
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        collectionView.delegate = self
        collectionView.dataSource = self
        colorViewHeight.constant = 120
    }
    
    @objc func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 5
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        if(indexPath.row == 0){
           let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "collectiontopcell", for: indexPath) as! ced_vendortopCollectionCell
            cell.backgroundColor = color
            cell.imageView.image = UIImage(named: "dashboardslider")
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorName = userData["vendorName"] as? String
            cell.bottomheading.text = "Hello,".localized + " \(vendorName!)"
            cell.topheading.text  = "Dashboard".localized
            return cell
        }else if(indexPath.row == 1){
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "collectiontopcell", for: indexPath) as! ced_vendortopCollectionCell
             cell.backgroundColor = UIColor.red
            cell.imageView.image = UIImage(named: "pendingcard")
            cell.bottomheading.text = "Transaction Total Pending Amount".localized
            cell.topheading.text  = slideData["pending_amount"]
            return cell
        }else if(indexPath.row == 2){
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "collectiontopcell", for: indexPath) as! ced_vendortopCollectionCell
             cell.backgroundColor = UIColor.green
            cell.imageView.image = UIImage(named: "earnedcard")
            cell.bottomheading.text = "Total Earned Amount".localized
            cell.topheading.text  = slideData["net_earned"]
            return cell
        }else if(indexPath.row == 3){
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "collectiontopcell", for: indexPath) as! ced_vendortopCollectionCell
             cell.backgroundColor = UIColor.orange
            cell.imageView.image = UIImage(named: "cart")
            cell.bottomheading.text = "Total Orders Placed".localized
            cell.topheading.text  = slideData["order_placed"]
            return cell
        }else {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "collectiontopcell", for: indexPath) as! ced_vendortopCollectionCell
             cell.backgroundColor = UIColor.blue
            cell.bottomheading.text = "Total Products Sold!".localized
            cell.imageView.image = UIImage(named: "bar")
               cell.topheading.text  = slideData["sold"]
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        if(indexPath.row == 1)
        {
            let storybordOrder = UIStoryboard(name: "Main", bundle: nil)
            let viewControl = storybordOrder.instantiateViewController(withIdentifier: "viewtransaction") as! ced_viewTransactions
            parent.navigationController?.pushViewController(viewControl, animated: true)
        }
        else if(indexPath.row == 2)
        {
            let storybordOrder = UIStoryboard(name: "Main", bundle: nil)
            let viewControl = storybordOrder.instantiateViewController(withIdentifier: "viewtransaction") as! ced_viewTransactions
            parent.navigationController?.pushViewController(viewControl, animated: true)
        }
        else if(indexPath.row == 3)
        {
            let storybordOrder = UIStoryboard(name: "Main", bundle: nil)
            let viewControl = storybordOrder.instantiateViewController(withIdentifier: "vendorOrders") as! ced_vendorOrderpannel
            parent.navigationController?.pushViewController(viewControl, animated: true)
        }
        else if(indexPath.row == 4)
        {
            let storybordOrder = UIStoryboard(name: "Main", bundle: nil)
            let viewControl = storybordOrder.instantiateViewController(withIdentifier: "productReportRangeViewController") as! ProductReportRangeViewController
            parent.navigationController?.pushViewController(viewControl, animated: true)
        }
    }
    
    @objc func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return collectionView.bounds.size
    }
    
    @objc func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
       let  pageWidth = self.collectionView.frame.size.width;
        self.pages.currentPage = Int(self.collectionView.contentOffset.x / pageWidth)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
