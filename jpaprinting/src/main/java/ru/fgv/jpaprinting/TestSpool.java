package ru.fgv.jpaprinting;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef.PVOID;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.Winspool;
import com.sun.jna.platform.win32.WinspoolUtil;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import ru.fgv.jpaprinting.TestSpool.WinSpool2.PRINTER_NOTIFY_INFO;
import ru.fgv.jpaprinting.TestSpool.WinSpool2.PRINTER_NOTIFY_INFO_DATA;
import ru.fgv.jpaprinting.TestSpool.WinSpool2.PRINTER_NOTIFY_OPTIONS;

/**
 *
 * @author Fgv
 */
public class TestSpool {
    
    public interface WinSpool2 extends Winspool {
		WinSpool2 INSTANCE = (WinSpool2) Native.loadLibrary("Winspool.drv", WinSpool2.class, W32APIOptions.UNICODE_OPTIONS);		
				
		HANDLE FindFirstPrinterChangeNotification(HANDLE hPrinter, int fdwFilter, int fdwOptions, Pointer pPrinterNotifyOptions);
		
		boolean FindNextPrinterChangeNotification(HANDLE hChange, IntByReference pdwChange, Pointer pPrinterNotifyOptions, Pointer ppPrinterNotifyInfo);
		
		public static class PRINTER_NOTIFY_OPTIONS extends Structure {
			
			public static class ByReference extends PRINTER_NOTIFY_OPTIONS implements Structure.ByReference { }
			
			public int Version;
			public int Flags;
			public int Count;
			public PRINTER_NOTIFY_OPTIONS_TYPE.ByReference pTypes;
			
			public PRINTER_NOTIFY_OPTIONS() {
				
			}
			
			public PRINTER_NOTIFY_OPTIONS(int size) {
				super(new Memory(size));
			}
			
			public PRINTER_NOTIFY_OPTIONS(int Version, int Flags, int Count, PRINTER_NOTIFY_OPTIONS_TYPE.ByReference pTypes) {
				this.Version = Version;
				this.Flags = Flags;
				this.Count = Count;
				this.pTypes = pTypes;
			}
			
			@SuppressWarnings("rawtypes")
			@Override
			protected List getFieldOrder() {
				return Arrays.asList(new String[] {"Version", "Flags", "Count", "pTypes"});
			}
			
		}
		
		public static class PRINTER_NOTIFY_OPTIONS_TYPE extends Structure {
			
			public static class ByReference extends PRINTER_NOTIFY_OPTIONS_TYPE implements Structure.ByReference { }
			
			public short Type;
			public short Reserved0;
			public int Reserved1;
			public int Reserved2;
			public int Count;
			public Pointer pFields;
			
			public PRINTER_NOTIFY_OPTIONS_TYPE() {
				
			}
			
			public PRINTER_NOTIFY_OPTIONS_TYPE(int size) {
				super(new Memory(size));
			}
			
			@SuppressWarnings("rawtypes")
			@Override
			protected List getFieldOrder() {
				return Arrays.asList(new String[] {"Type","Reserved0","Reserved1","Reserved2","Count","pFields"});
			}
			
		}
		
		public static class PRINTER_NOTIFY_INFO extends Structure {

			public int Version;
			public int Flags;
			public int Count;
			public PRINTER_NOTIFY_INFO_DATA[] aData = new PRINTER_NOTIFY_INFO_DATA[1];
			
			public PRINTER_NOTIFY_INFO() {
				super();
			}
			
			public PRINTER_NOTIFY_INFO(int Version, int Flags, int Count, PRINTER_NOTIFY_INFO_DATA aData[]) {
				super();
				this.Version = Version;
				this.Flags = Flags;
				this.Count = Count;
				if ((aData.length != this.aData.length)) 
					throw new IllegalArgumentException("Wrong array size !");
				this.aData = aData;
			}
			
			public static class ByReference extends PRINTER_NOTIFY_INFO implements Structure.ByReference {}
			public static class ByValue extends PRINTER_NOTIFY_INFO implements Structure.ByValue {}
			
			@SuppressWarnings("rawtypes")
                        @Override
			protected List getFieldOrder() {
				return Arrays.asList("Version", "Flags", "Count", "aData");
			}
			
		}
		
		public static class PRINTER_NOTIFY_INFO_DATA extends Structure {

			public short Type;
			public short Field;
			public int Reserved;
			public int Id;
			public NotifyData_union NotifyData;
			
			public static class NotifyData_union extends Union {
				
				public int[] adwData = new int[2];
				public Data_struct Data;
				
				public static class Data_struct extends Structure {

					public int cbBuf;
					public PVOID pBuf;
					
					public Data_struct() {
						super();
					}
					
					public Data_struct(int cbBuf, PVOID pBuf) {
						super();
						this.cbBuf = cbBuf;
						this.pBuf = pBuf;
					}
					
                                        @Override
					protected List<? > getFieldOrder() {
						return Arrays.asList("cbBuf", "pBuf");
					}
					
					public static class ByReference extends Data_struct implements Structure.ByReference {}
					public static class ByValue extends Data_struct implements Structure.ByValue {}
				}
				
				public NotifyData_union() {
					super();
				}
				
				public NotifyData_union(Data_struct Data) {
					super();
					this.Data = Data;
					setType(Data_struct.class);
				}
				
				public NotifyData_union(int adwData[]) {
					super();
					if ((adwData.length != this.adwData.length)) 
						throw new IllegalArgumentException("Wrong array size !");
					this.adwData = adwData;
					setType(int[].class);
				}
				
				public static class ByReference extends NotifyData_union implements Structure.ByReference {}
				public static class ByValue extends NotifyData_union implements Structure.ByValue {}
			}
			
			
			public PRINTER_NOTIFY_INFO_DATA() {
				super();
			}
			
			public PRINTER_NOTIFY_INFO_DATA(short Type, short Field, int Reserved, int Id, NotifyData_union NotifyData) {
				super();
				this.Type = Type;
				this.Field = Field;
				this.Reserved = Reserved;
				this.Id = Id;
				this.NotifyData = NotifyData;
			}
			
			public static class ByReference extends PRINTER_NOTIFY_INFO_DATA implements Structure.ByReference {}
			public static class ByValue extends PRINTER_NOTIFY_INFO_DATA implements Structure.ByValue {}
			
			@SuppressWarnings("rawtypes")
                        @Override
			protected List getFieldOrder() {
				return Arrays.asList("Type", "Field", "Reserved", "Id", "NotifyData");
			}
		}
			
			
		int PRINTER_CHANGE_ADD_FORM	= 0x00010000;
		int PRINTER_CHANGE_SET_FORM	= 0x00020000;
		int PRINTER_CHANGE_DELETE_FORM	= 0x00040000;
		int PRINTER_CHANGE_FORM = 0x00070000;
		int PRINTER_CHANGE_ADD_JOB = 0x00000100;
		int PRINTER_CHANGE_SET_JOB = 0x00000200;
		int PRINTER_CHANGE_DELETE_JOB = 0x00000400;
		int PRINTER_CHANGE_WRITE_JOB = 0x00000800;
		int PRINTER_CHANGE_JOB = 0x0000FF00;
		int PRINTER_CHANGE_ADD_PORT	= 0x00100000;
		int PRINTER_CHANGE_CONFIGURE_PORT = 0x00200000;
		int PRINTER_CHANGE_DELETE_PORT = 0x00400000;
		int PRINTER_CHANGE_PORT	= 0x00700000;
		int PRINTER_CHANGE_ADD_PRINT_PROCESSOR = 0x01000000;
		int PRINTER_CHANGE_DELETE_PRINT_PROCESSOR =	0x04000000;
		int PRINTER_CHANGE_PRINT_PROCESSOR = 0x07000000;
		int PRINTER_CHANGE_ADD_PRINTER = 0x00000001;
		int PRINTER_CHANGE_SET_PRINTER = 0x00000002;
		int PRINTER_CHANGE_DELETE_PRINTER = 0x00000004;
		int PRINTER_CHANGE_FAILED_CONNECTION_PRINTER = 0x00000008;
		int PRINTER_CHANGE_PRINTER = 0x000000FF;
		int PRINTER_CHANGE_ADD_PRINTER_DRIVER =	0x10000000;
		int PRINTER_CHANGE_SET_PRINTER_DRIVER =	0x20000000;
		int PRINTER_CHANGE_DELETE_PRINTER_DRIVER = 0x40000000;
		int PRINTER_CHANGE_PRINTER_DRIVER =	0x70000000;
		int PRINTER_CHANGE_ALL = 0x7777FFFF;
	
	}
	
public static void main(String[] args) {
	
	PRINTER_NOTIFY_OPTIONS options = new PRINTER_NOTIFY_OPTIONS();
	options.Flags = 1; //PRINTER_NOTIFY_OPTIONS_REFRESH
	options.Version = 2;
		
        String pPrinterName = "HP LaserJet P2050 Series PCL6";
        WinNT.HANDLEByReference handleByRef = new WinNT.HANDLEByReference();
        Winspool.INSTANCE.OpenPrinter(pPrinterName, handleByRef, null);
        
	HANDLE handle = WinSpool2.INSTANCE.FindFirstPrinterChangeNotification(handleByRef.getValue(), WinSpool2.PRINTER_CHANGE_ADD_JOB, 0, options.getPointer());
	if(handle == null)
		throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
	
	// Wait until you send a job to the print queue
	if (Kernel32.INSTANCE.WaitForSingleObject(handle, WinBase.INFINITE) == WinBase.WAIT_OBJECT_0) {
		// Job sent to print queue
		IntByReference pdwChange = new IntByReference();
		PRINTER_NOTIFY_INFO ppPrinterNotifyInfo = new PRINTER_NOTIFY_INFO();
		if (WinSpool2.INSTANCE.FindNextPrinterChangeNotification(handle, pdwChange, options.getPointer(), ppPrinterNotifyInfo.getPointer())) {
			Winspool.JOB_INFO_1[] jobInfo1 = WinspoolUtil.getJobInfo1(handleByRef);
                    for (Winspool.JOB_INFO_1 jobInfo11 : jobInfo1) {
                        TestSpool.printJobInfo(jobInfo11);
                    }
		}
	
        }}
        
        private static void printJobInfo(Winspool.JOB_INFO_1 jobInfo1) {
        WinBase.FILETIME lpFileTime = new WinBase.FILETIME();
        Kernel32.INSTANCE.SystemTimeToFileTime(jobInfo1.Submitted, lpFileTime);

        String info = "JobId: " + jobInfo1.JobId + "\n" + "pDatatype: "
                + jobInfo1.pDatatype + "\n" + "PagesPrinted: "
                + jobInfo1.PagesPrinted + "\n" + "pDocument: "
                + jobInfo1.pDocument + "\n" + "pMachineName: "
                + jobInfo1.pMachineName + "\n" + "Position: "
                + jobInfo1.Position + "\n" + "pPrinterName: "
                + jobInfo1.pPrinterName + "\n" + "Priority: "
                + jobInfo1.Priority + "\n" + "pStatus: "
                + jobInfo1.pStatus + "\n" + "pUserName: "
                + jobInfo1.pUserName + "\n" + "Status: "
                + jobInfo1.Status + "\n" + "TotalPages: "
                + jobInfo1.TotalPages
                + "\n" + "Submitted: " + DateFormat.getDateTimeInstance().format(lpFileTime.toDate());

        LOG.info(info);
    }
    private static final Logger LOG = Logger.getLogger(TestSpool.class.getName());


}
