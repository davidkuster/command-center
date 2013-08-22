package grailscommandcenter.processimpl

import com.sun.jna.Library
import com.sun.jna.Platform
import com.sun.jna.Pointer
import com.sun.jna.Native
import com.sun.jna.Structure
import com.sun.jna.platform.win32.BaseTSD
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT

/**
 * using code and ideas from:
 * http://stackoverflow.com/questions/4912282
 * http://stackoverflow.com/questions/10124299
 * http://www.golesny.de/p/code/javagetpid
 *
 * JNA javadoc: http://twall.github.com/jna/3.4.1/javadoc/
 */
interface JnaWrapper extends Library {

	// param used for call to OpenProcess - rights to terminate & query information
	static int ACCESS_OPTIONS = WinNT.PROCESS_TERMINATE | 0x0400;

	WinDef.DWORD TH32CS_SNAPPROCESS  = new WinDef.DWORD(0x00000002);

	// TODO: should probably only load this when Platform.isWindows()
	public static JnaWrapper INSTANCE = (JnaWrapper) Native.loadLibrary("kernel32", JnaWrapper.class) 


	public int GetProcessId(Long hProcess)

	public boolean TerminateProcess(WinNT.HANDLE hProcess, int exitCode)

	public int GetLastError()

	// gets a handle to an existing process
	public WinNT.HANDLE OpenProcess(int fdwAccess, boolean fInherit, int IDProcess)

	// gets snapshot of the specified processes
	public WinNT.HANDLE CreateToolhelp32Snapshot( WinDef.DWORD dwFlags, WinDef.DWORD th32ProcessID )

	// retrieves info about first process encountered in a system snapshot
	public boolean Process32First( WinNT.HANDLE hSnap, JnaWrapper.PROCESSENTRY32.ByReference lppe )

	// retrieves info about the next process encountered in a system snapshot
	public boolean Process32Next( WinNT.HANDLE hSnap, JnaWrapper.PROCESSENTRY32.ByReference lppe )



	public static class PROCESSENTRY32 extends Structure {

        public static class ByReference extends PROCESSENTRY32 implements Structure.ByReference {
            public ByReference() {
            }

            public ByReference(Pointer memory) {
                super(memory);
            }
        }

        public PROCESSENTRY32() {
            dwSize = new WinDef.DWORD(size());
        }

        public PROCESSENTRY32(Pointer memory) {
            useMemory(memory);
            read();
        }

        /**
         * The size of the structure, in bytes. Before calling the Process32First function, set this member to
         * sizeof(PROCESSENTRY32). If you do not initialize dwSize, Process32First fails.
         */
        public WinDef.DWORD dwSize;

        /**
         * This member is no longer used and is always set to zero.
         */
        public WinDef.DWORD cntUsage;

        /**
         * The process identifier.
         */
        public WinDef.DWORD th32ProcessID;

        /**
         * This member is no longer used and is always set to zero.
         */
        public BaseTSD.ULONG_PTR th32DefaultHeapID;

        /**
         * This member is no longer used and is always set to zero.
         */
        public WinDef.DWORD th32ModuleID;

        /**
         * The number of execution threads started by the process.
         */
        public WinDef.DWORD cntThreads;

        /**
         * The identifier of the process that created this process (its parent process).
         */
        public WinDef.DWORD th32ParentProcessID;

        /**
         * The base priority of any threads created by this process.
         */
        public WinDef.LONG pcPriClassBase;

        /**
         * This member is no longer used, and is always set to zero.
         */
        public WinDef.DWORD dwFlags;

        /**
         * The name of the executable file for the process. To retrieve the full path to the executable file, call the
         * Module32First function and check the szExePath member of the MODULEENTRY32 structure that is returned.
         * However, if the calling process is a 32-bit process, you must call the QueryFullProcessImageName function to
         * retrieve the full path of the executable file for a 64-bit process.
         */
        public char[] szExeFile = new char[WinDef.MAX_PATH];
    }


}